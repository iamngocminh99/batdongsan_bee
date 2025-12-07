package com.ngocminh.batdongsan_be.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String planName;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private Double planPrice;
    private boolean paid;
}

