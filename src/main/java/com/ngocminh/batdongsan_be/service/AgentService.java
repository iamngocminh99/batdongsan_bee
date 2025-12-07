package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.repository.AgentRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    public Agent create(Agent agent) {
        // check user tồn tại
        UUID userId = agent.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        agent.setUser(user);
        return agentRepository.save(agent);
    }

    public Optional<Agent> getById(UUID id) {
        return agentRepository.findById(id);
    }


    public Agent update(UUID id, Agent details) {
        return agentRepository.findById(id).map(a -> {
            a.setCompanyName(details.getCompanyName());
            a.setDescription(details.getDescription());
            a.setLogo(details.getLogo());
            a.setRating(details.getRating());
            a.setTotalReviews(details.getTotalReviews());
            return agentRepository.save(a);
        }).orElseThrow(() -> new RuntimeException("Agent not found"));
    }

    public void delete(UUID id) {
        agentRepository.deleteById(id);
    }

    public Map<String, Object> getAgentStats(Integer day, Integer month, Integer year) {

        // 1. XÁC ĐỊNH KHOẢNG THỜI GIAN
        LocalDate start;
        LocalDate end;

        if (day != null && month != null && year != null) {
            start = LocalDate.of(year, month, day);
            end = start.plusDays(1);
        } else if (month != null && year != null) {
            start = LocalDate.of(year, month, 1);
            end = start.plusMonths(1);
        } else if (year != null) {
            start = LocalDate.of(year, 1, 1);
            end = start.plusYears(1);
        } else {
            // mặc định tháng hiện tại
            LocalDate now = LocalDate.now();
            start = now.withDayOfMonth(1);
            end = start.plusMonths(1);
        }

        // Convert LocalDate → LocalDateTime (fix lỗi Hibernate)
        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt = end.atStartOfDay();

        // 2. LẤY SỐ LIỆU
        long totalAgents = agentRepository.countByPlanStartDateBetween(startDt, endDt);

        // Thống kê theo từng loại gói
        Map<String, Long> planStats = Arrays.stream(Agent.PlanType.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        type -> agentRepository.countByPlanNameAndPlanStartDateBetween(type, startDt, endDt)
                ));

        // Revenue – tránh NULL
        Double revenue = agentRepository.sumPlanPriceBetween(startDt, endDt);
        if (revenue == null) revenue = 0.0;

        // GROUP BY city
        List<Map<String, Object>> cityStats = agentRepository.countGroupByCity(startDt, endDt);

        // 3. TRẢ VỀ
        return Map.of(
                "totalAgents", totalAgents,
                "planStats", planStats,
                "totalRevenue", revenue,
                "cityStats", cityStats
        );
    }


}
