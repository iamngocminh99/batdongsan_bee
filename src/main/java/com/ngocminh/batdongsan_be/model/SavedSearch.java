//package com.ngocminh.batdongsan_be.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.annotations.UuidGenerator;
//import org.hibernate.type.SqlTypes;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "saved_searches")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class SavedSearch {
//
//    @Id
//    @UuidGenerator
//    @JdbcTypeCode(SqlTypes.CHAR)
//    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
//    UUID id;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private User user;
//
//    private String name;
//
//    @ManyToOne
//    @JoinColumn(name = "locationId")
//    private Location location;
//
//    private Double minPrice;
//    private Double maxPrice;
//    private Integer minBedrooms;
//    private Integer maxBedrooms;
//
//    @Enumerated(EnumType.STRING)
//    private AlertFrequency alertFrequency;
//
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    public enum AlertFrequency {
//        DAILY, WEEKLY, MONTHLY
//    }
//}
