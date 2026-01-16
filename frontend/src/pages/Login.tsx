import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const DEMO_EMAIL = 'admin@example.com';
const DEMO_PASSWORD = 'admin123!';

const Login = () => {
  const [email, setEmail] = useState(DEMO_EMAIL);
  const [password, setPassword] = useState(DEMO_PASSWORD);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    if (email === DEMO_EMAIL && password === DEMO_PASSWORD) {
      localStorage.setItem('demo_auth', 'true');
      setError('');
      navigate('/admin');
      return;
    }
    setError('Identifiants invalides.');
  };

  return (
    <div className="page">
      <div className="card">
        <h1>Connexion</h1>
        <p className="muted">Acces administrateur (mode demo).</p>
        <form onSubmit={handleSubmit} className="form">
          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
            />
          </label>
          <label>
            Mot de passe
            <input
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
            />
          </label>
          {error ? <div className="error">{error}</div> : null}
          <button type="submit">Se connecter</button>
        </form>
        <div className="hint">
          Identifiants: <strong>{DEMO_EMAIL}</strong> /{' '}
          <strong>{DEMO_PASSWORD}</strong>
        </div>
      </div>
    </div>
  );
};

export default Login;
