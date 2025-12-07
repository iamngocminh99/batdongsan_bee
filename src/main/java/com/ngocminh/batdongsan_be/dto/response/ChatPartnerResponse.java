package com.ngocminh.batdongsan_be.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatPartnerResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}