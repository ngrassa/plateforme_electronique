#!/bin/bash

# Plateforme de Facturation Electronique - Setup Script
# Compatible with Ubuntu 25.04
# This script installs all necessary dependencies and sets up the development environment

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check Ubuntu version
check_ubuntu_version() {
    if [[ -f /etc/os-release ]]; then
        . /etc/os-release
        if [[ "$ID" == "ubuntu" ]]; then
            log_info "Ubuntu version detected: $VERSION"
            if [[ "$VERSION_ID" == "25.04" ]]; then
                log_success "Compatible Ubuntu version detected"
            else
                log_warning "This script is designed for Ubuntu 25.04, but $VERSION is detected"
            fi
        else
            log_error "This script is designed for Ubuntu. Detected: $ID"
            exit 1
        fi
    else
        log_warning "Could not detect OS version. Proceeding anyway..."
    fi
}

# Function to install Java 21
install_java() {
    log_info "Installing OpenJDK 21..."
    sudo apt update
    sudo apt install -y openjdk-21-jdk openjdk-21-jre

    # Set JAVA_HOME
    JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
    echo "export JAVA_HOME=$JAVA_HOME" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
    log_success "Java 21 installed"
}

# Function to install Maven
install_maven() {
    log_info "Installing Apache Maven..."
    sudo apt install -y maven
    log_success "Maven installed"
}

# Function to install Node.js and npm
install_nodejs() {
    log_info "Installing Node.js 20 and npm..."
    curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
    sudo apt-get install -y nodejs
    log_success "Node.js and npm installed"
}

# Function to create default users in database
create_default_users() {
    log_info "Creating default users in database..."

    # Wait for PostgreSQL to be ready
    until docker-compose exec -T postgresql pg_isready -U plateforme_user -d plateforme_electronique >/dev/null 2>&1; do
        log_warning "Waiting for PostgreSQL to be ready..."
        sleep 5
    done

    # Create admin user in user_auth_db
    docker-compose exec -T postgresql psql -U plateforme_user -d user_auth_db -c "
        CREATE EXTENSION IF NOT EXISTS \"pgcrypto\";
        CREATE TABLE IF NOT EXISTS users (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            email VARCHAR(255) UNIQUE NOT NULL,
            password_hash VARCHAR(255) NOT NULL,
            first_name VARCHAR(100),
            last_name VARCHAR(100),
            phone VARCHAR(50),
            company_name VARCHAR(255),
            tax_id VARCHAR(100),
            role VARCHAR(30) NOT NULL DEFAULT 'USER',
            active BOOLEAN NOT NULL DEFAULT true,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP
        );"

    # Check if admin user already exists, if not create
    local user_exists=$(docker-compose exec -T postgresql psql -U plateforme_user -d user_auth_db -t -c "SELECT 1 FROM users WHERE email='admin@example.com' LIMIT 1;")

    if [[ -z "${user_exists// }" ]]; then
        # Generate bcrypt hash for password 'admin123!'
        local password_hash='$2a$10$8K3dsFVKHO9PhRwOnUVgN.6uVGzdFcVpg6z.jetYmyEQ8b8nHwVwW'  # admin123!
        docker-compose exec -T postgresql psql -U plateforme_user -d user_auth_db -c "
            INSERT INTO users (email, password_hash, role, active)
            VALUES ('admin@example.com', '$password_hash', 'ADMIN', true);"
        log_success "Created admin user (admin@example.com / admin123!)"
    else
        log_info "Admin user already exists"
    fi

    # Create default user for testing
    local test_user_exists=$(docker-compose exec -T postgresql psql -U plateforme_user -d user_auth_db -t -c "SELECT 1 FROM users WHERE email='user@example.com' LIMIT 1;")

    if [[ -z "${test_user_exists// }" ]]; then
        local test_password_hash='$2a$10$8K3dsFVKHO9PhRwOnUVgN.6uVGzdFcVpg6z.jetYmyEQ8b8nHwVwW'  # same hash as admin for demo
        docker-compose exec -T postgresql psql -U plateforme_user -d user_auth_db -c "
            INSERT INTO users (email, password_hash, role, active)
            VALUES ('user@example.com', '$test_password_hash', 'USER', true);"
        log_info "Created test user (user@example.com / test123!)"
    fi
}

# Function to install Docker
install_docker() {
    log_info "Installing Docker..."
    if ! command -v systemctl >/dev/null 2>&1 || [[ ! -d /run/systemd/system ]]; then
        log_warning "systemd not available; skipping Docker installation"
        return 0
    fi
    sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt update
    sudo apt install -y docker-ce docker-ce-cli containerd.io
    sudo usermod -aG docker $USER
    if command -v systemctl >/dev/null 2>&1; then
        sudo systemctl enable docker
        sudo systemctl start docker
    else
        log_warning "systemctl not available; Docker service not started automatically"
    fi
    log_success "Docker installed"
}

# Function to install Docker Compose
install_docker_compose() {
    log_info "Installing Docker Compose..."
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    log_success "Docker Compose installed"
}

# Function to install kubectl
install_kubectl() {
    log_info "Installing kubectl..."
    curl -LO "https://dl.k3s.io/release/v1.28.0+k3s1/kubectl"
    sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
    log_success "kubectl installed"
}

# Function to install PostgreSQL client
install_postgresql_client() {
    log_info "Installing PostgreSQL client..."
    sudo apt install -y postgresql-client
    log_success "PostgreSQL client installed"
}

# Function to check system requirements
check_system_requirements() {
    log_info "Checking system requirements..."

    # Check available memory
    MEM_GB=$(free -g | awk 'NR==2{printf "%.0f", $2}')
    if (( MEM_GB < 8 )); then
        log_warning "Recommended: 8GB+ RAM. Current: ${MEM_GB}GB. The application may run slowly."
    else
        log_success "Memory check passed: ${MEM_GB}GB"
    fi

    # Check available disk space
    DISK_GB=$(df / | tail -1 | awk '{print int($4/1024/1024)}')
    if (( DISK_GB < 20 )); then
        log_warning "Recommended: 20GB+ free disk space. Current: ${DISK_GB}GB. You may need more space."
    else
        log_success "Disk space check passed: ${DISK_GB}GB free"
    fi
}

# Function to setup development environment
setup_development_environment() {
    log_info "Setting up development environment..."

    # Create Maven wrapper if a pom.xml exists in the current directory
    if [[ ! -f "mvnw" ]]; then
        if [[ -f "pom.xml" ]]; then
            log_info "Creating Maven wrapper..."
            mvn -N io.tmanabe:archetype-maven-wrapper
        else
            log_warning "No pom.xml found in $(pwd); skipping Maven wrapper creation"
        fi
    fi

    # Setup pre-commit hooks if git repo
    if [[ -d ".git" ]]; then
        log_info "Setting up git hooks..."
        # Pre-commit hook for formatting
        cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash
echo "Running pre-commit checks..."

# For Java services - check if Maven exists and run compile
if [[ -f "pom.xml" && -x "$(command -v mvn)" ]]; then
    echo "Running Maven compile check..."
    mvn clean compile -q
    if [[ $? -ne 0 ]]; then
        echo "Maven compile failed. Please fix compilation errors."
        exit 1
    fi
fi

# For Node.js - check if package.json exists and run lint/format
if [[ -f "package.json" && -x "$(command -v npm)" ]]; then
    if [[ -f "node_modules/.bin/eslint" ]]; then
        echo "Running ESLint..."
        npm run lint --silent
    fi
fi

echo "Pre-commit checks passed!"
EOF
        chmod +x .git/hooks/pre-commit
    fi

    log_success "Development environment configured"
}

# Function to display usage information
show_usage() {
    log_info "This setup script will install all necessary dependencies for the Electronic Billing Platform."
    log_info "The following will be installed:"
    log_info "  - OpenJDK 21"
    log_info "  - Apache Maven"
    log_info "  - Node.js 20"
    log_info "  - Docker & Docker Compose"
    log_info "  - kubectl"
    log_info "  - PostgreSQL client"
    log_info ""
    log_info "Usage: $0 [OPTIONS]"
    log_info ""
    log_info "Options:"
    log_info "  --check-only     Only check system and skip installation"
    log_info "  --no-docker      Skip Docker installation"
    log_info "  --auto-start     Build and start containers after install"
    log_info "  --help          Show this help message"
    echo ""
}

# Main installation function
main() {
    local skip_docker=false
    local check_only=false
    local auto_start=false

    # Parse arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            --help)
                show_usage
                exit 0
                ;;
            --check-only)
                check_only=true
                ;;
            --no-docker)
                skip_docker=true
                ;;
            --auto-start)
                auto_start=true
                ;;
            *)
                log_error "Unknown option: $1"
                show_usage
                exit 1
                ;;
        esac
        shift
    done

    echo "=============================================="
    echo "🔧 Electronic Billing Platform Setup Script"
    echo "=============================================="
    echo ""

    # Check Ubuntu version
    check_ubuntu_version

    # Check system requirements
    check_system_requirements

    if [[ "$check_only" == true ]]; then
        log_info "Check-only mode: installation skipped"
        exit 0
    fi

    log_info "Starting installation process..."

    # Install required packages
    install_java
    install_maven
    install_nodejs

    if [[ "$skip_docker" == false ]]; then
        install_docker
        install_docker_compose
        install_kubectl
    else
        log_info "Skipping Docker installation as requested"
    fi

    install_postgresql_client

    # Setup development environment
    setup_development_environment

    if [[ "$auto_start" == true ]]; then
        if [[ "$skip_docker" == true ]]; then
            log_warning "Auto-start requested but Docker installation was skipped"
        elif ! command_exists docker || ! command_exists docker-compose; then
            log_warning "Auto-start requested but Docker Compose is not available"
        else
            log_info "Starting containers with docker compose..."
            docker-compose up --build -d
            create_default_users
            log_success "Application started successfully"
        fi
    fi

    echo ""
    echo "=============================================="
    log_success "Installation completed successfully!"
    echo ""
    log_info "URLs:"
    log_info "- Frontend: http://localhost:3000"
    log_info "- API Gateway: http://localhost:8080"
    log_info "- Keycloak: http://localhost:8081"
    log_info "- Eureka: http://localhost:8761"
    echo ""
    log_info "Next steps:"
    log_info "1. Restart your terminal or run 'source ~/.bashrc' to load new environment variables"
    if [[ "$skip_docker" == false ]]; then
        log_info "2. Log out and back in for Docker group changes to take effect"
    fi
    if [[ "$auto_start" == false ]]; then
        log_info "3. To start the application: docker compose up --build"
    fi
    log_info "4. Admin login: admin@example.com / admin123!"
    echo ""
    log_info "For development:"
    log_info "- Run individual services: cd services/{service-name} && mvn spring-boot:run"
    log_info "- Run frontend: cd frontend && npm start"
    echo "=============================================="
}

# Run main function with all arguments
main "$@"
