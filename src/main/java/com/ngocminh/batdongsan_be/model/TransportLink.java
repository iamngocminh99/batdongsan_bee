//package com.ngocminh.batdongsan_be.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.annotations.UuidGenerator;
//import org.hibernate.type.SqlTypes;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "transport_links")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class TransportLink {
//
//    @Id
//    @UuidGenerator
//    @JdbcTypeCode(SqlTypes.CHAR)
//    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
//    UUID id;
//
//    private String stationName;
//    private Double distance;    // km
//    private int walkingTime;   // minutes
//
//    @Enumerated(EnumType.STRING)
//    private TransportType type;
//
//    @ManyToOne
//    @JoinColumn(name = "propertyId")
//    private Property property;
//
//    public enum TransportType {
//        BUS, MRT, TRAIN, METRO, AIRPORT
//    }
//}
//
