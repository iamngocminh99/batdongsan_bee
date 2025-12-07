package com.ngocminh.batdongsan_be.dto.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
