package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.service.AgentService;
import com.ngocminh.batdongsan_be.service.PaymentService;
import com.ngocminh.batdongsan_be.service.PropertyService;
import com.ngocminh.batdongsan_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final PropertyService propertyService;
    private final PaymentService paymentService;
    private final AgentService agentService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary() {
        long totalUsers = userService.countByRole(User.Role.USER);
        long totalAgents = userService.countByRole(User.Role.AGENT);
        long totalProperties = propertyService.countProperties();
        BigDecimal totalRevenue = paymentService.getTotalRevenue();

        return ResponseEntity.ok(Map.of(
                "totalUsers", totalUsers,
                "totalAgents", totalAgents,
                "totalProperties", totalProperties,
                "totalRevenue", totalRevenue
        ));
    }

    @GetMapping("/monthly-revenue")
    public List<Map<String, Object>> getMonthlyRevenue() {
        return paymentService.getMonthlyRevenue();
    }

    @GetMapping("/properties/stats")
    public ResponseEntity<Map<String, Object>> getPropertyStats(
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(propertyService.getPropertyStats(day, month, year));
    }

    @GetMapping("/agents/stats")
    public ResponseEntity<Map<String, Object>> getAgentStats(
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(agentService.getAgentStats(day, month, year));
    }

}

