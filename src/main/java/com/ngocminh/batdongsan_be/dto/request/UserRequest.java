package com.ngocminh.batdongsan_be.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class UserRequest {
    private UUID id;          // dùng cho update
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
