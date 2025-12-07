package com.ngocminh.batdongsan_be.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentResponse {
    private String companyName;
    private String mobile;
    private String address;
    private String city;
    private String logo;
    private float rating;
    private int totalReviews;
    private int propertiesSold;
    private int propertiesLet;
    private String description;

    private String planName;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private Double planPrice;
    private Integer maxProperties;
}
