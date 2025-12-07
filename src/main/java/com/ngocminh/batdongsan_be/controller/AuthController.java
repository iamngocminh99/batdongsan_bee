package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.dto.request.AgentRequest;
import com.ngocminh.batdongsan_be.dto.request.AuthRequest;
import com.ngocminh.batdongsan_be.dto.response.AuthResponse;
import com.ngocminh.batdongsan_be.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/register/agent")
    public ResponseEntity<?> registerAgent(@RequestBody AgentRequest request) {
        return ResponseEntity.ok(authService.registerAgent(request));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String jwt = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}


