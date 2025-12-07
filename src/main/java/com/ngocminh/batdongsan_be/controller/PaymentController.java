package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment/vnpay")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(
            @RequestParam UUID userId,
            @RequestParam String planType,
            @RequestParam BigDecimal amount,
            HttpServletRequest request
    ) throws Exception {
        String url = paymentService.createPaymentUrl(request, amount, userId, planType);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/callback")
    public void vnpayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        paymentService.handleVNPayCallback(request, response);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, BigDecimal>> getTotalRevenue() {
        BigDecimal totalRevenue = paymentService.getTotalRevenue();
        return ResponseEntity.ok(Map.of("totalRevenue", totalRevenue));
    }

}
