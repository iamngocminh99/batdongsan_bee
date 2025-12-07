package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {

    // Lấy tất cả agent có ngày hết hạn nhỏ hơn thời gian hiện tại
    List<Agent> findByPlanEndDateBefore(LocalDateTime now);

    Optional<Agent> findByUser(User user);

    // Đếm tổng số agent đã mua gói trong khoảng thời gian
    long countByPlanStartDateBetween(LocalDateTime start, LocalDateTime end);

    // Đếm agent theo từng loại gói trong khoảng thời gian
    long countByPlanNameAndPlanStartDateBetween(Agent.PlanType planType,
                                                LocalDateTime start,
                                                LocalDateTime end);

    // Tổng doanh thu (NULL thì service đã handle)
    @Query("""
        SELECT SUM(a.planPrice)
        FROM Agent a
        WHERE a.planStartDate BETWEEN :start AND :end
    """)
    Double sumPlanPriceBetween(LocalDateTime start, LocalDateTime end);

    // Thống kê agent theo city (group by city)
    @Query("""
        SELECT new map(a.city as city, COUNT(a) as total)
        FROM Agent a
        WHERE a.planStartDate BETWEEN :start AND :end
        GROUP BY a.city
    """)
    List<Map<String, Object>> countGroupByCity(LocalDateTime start, LocalDateTime end);

}
