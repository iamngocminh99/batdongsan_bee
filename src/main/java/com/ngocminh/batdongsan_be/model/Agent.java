package com.ngocminh.batdongsan_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private UUID id;

    private String companyName;
    private String mobile;
    private String address;
    private String city;
    private String logo;
    private float rating;
    private int totalReviews;
    private int propertiesSold;
    private int propertiesLet;

    // phí
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planName;           // "REE", "MONTHLY", "YEARLY", "THREE_YEAR"
    private LocalDateTime planStartDate; // ngày bắt đầu
    private LocalDateTime planEndDate;   // ngày hết hạn
    private Double planPrice;            // giá gói
    private boolean paid; // thanh toán chưa
    @Column(name = "max_properties", nullable = false)
    private Integer maxProperties;

    // Cờ đánh dấu đã gửi email cảnh báo sắp hết hạn hay chưa
    @Column(nullable = false)
    private boolean notifiedExpiring = false;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private java.util.List<String> serviceAreas;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public enum PlanType {
        FREE, MONTHLY, YEARLY, THREE_YEAR
    }
}


