package com.ngocminh.batdongsan_be.repository;


import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    long countByRole(User.Role role);

    boolean existsByEmail(String email);

    Optional<User> findByEmailVerificationToken(String token);

    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<User> findByRole(User.Role role, Pageable pageable);

    Page<User> findByEmailContainingIgnoreCaseAndRole(String email, User.Role role, Pageable pageable);

    List<User> findByRoleIn(List<User.Role> roles);

    //  lọc theo gói
    Page<User> findByAgent_PlanName(Agent.PlanType planName, Pageable pageable);

    // lọc user sắp hết hạn gói (còn ≤ 7 ngày)
    Page<User> findByAgent_PlanEndDateBefore(LocalDateTime endDate, Pageable pageable);

}
