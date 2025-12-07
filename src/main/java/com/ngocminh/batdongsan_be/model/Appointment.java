//package com.ngocminh.batdongsan_be.model;
//import jakarta.persistence.*;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.annotations.UuidGenerator;
//import org.hibernate.type.SqlTypes;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "appointments")
//public class Appointment {
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
//    private LocalDateTime appointmentTime;
//
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
//    public enum Status {
//        PENDING,
//        CONFIRMED,
//        CANCELLED,
//        COMPLETED
//    }
//}
//
