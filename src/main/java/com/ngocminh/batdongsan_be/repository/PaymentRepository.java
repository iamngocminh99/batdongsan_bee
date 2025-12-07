package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByTxnRef(String txnRef);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS'")
    BigDecimal getTotalRevenue();

    @Query("""
        SELECT MONTH(p.createdAt) AS month, SUM(p.amount)
        FROM Payment p
        WHERE p.status = 'SUCCESS'
        GROUP BY MONTH(p.createdAt)
        ORDER BY MONTH(p.createdAt)
    """)
    List<Object[]> findMonthlyRevenue();
}
