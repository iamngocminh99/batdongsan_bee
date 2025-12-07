package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.Property;
import com.ngocminh.batdongsan_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID>, JpaSpecificationExecutor<Property> {
    List<Property> findByUserId(UUID userId);

    @Query("SELECT p.user FROM Property p WHERE p.id = :propertyId")
    Optional<User> findOwnerByPropertyId(UUID propertyId);

    long countByUser_Agent_Id(UUID agentId);

    long countByUser_Agent_IdAndCreatedAtBetween(
            UUID agentId, LocalDateTime start, LocalDateTime end
    );

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    long countBySaleTypeAndCreatedAtBetween(Property.SaleType saleType,
                                            LocalDateTime start, LocalDateTime end);

    long countByStatusAndCreatedAtBetween(Property.Status status,
                                          LocalDateTime start, LocalDateTime end);




}
