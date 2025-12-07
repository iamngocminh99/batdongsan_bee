package com.ngocminh.batdongsan_be.dto.request;

import lombok.Data;

@Data
public class AgentRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    private String companyName;
    private String address;
    private String city;
    private String mobile;
    private String logo;
    private String description;
}
