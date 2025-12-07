package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findByType(Plan.PlanType type);
}
