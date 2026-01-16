package com.plateforme.electronique.subscription.repository;

import com.plateforme.electronique.subscription.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByName(Plan.Name name);
}
