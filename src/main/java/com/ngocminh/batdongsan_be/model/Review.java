//package com.ngocminh.batdongsan_be.model;
//
//import jakarta.persistence.*;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.annotations.UuidGenerator;
//import org.hibernate.type.SqlTypes;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "reviews")
//public class Review {
//    @Id
//    @UuidGenerator
//    @JdbcTypeCode(SqlTypes.CHAR)
//    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
//    UUID id;
//
//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private Property property;
//
//    private int rating;
//    private String comment;
//    private LocalDateTime createdAt;
//}
//
