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
//@Table(name = "locations")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Location {
//
//    @Id
//    @UuidGenerator
//    @JdbcTypeCode(SqlTypes.CHAR)
//    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
//    UUID id;
//
//    private String name;
//
//    @Enumerated(EnumType.STRING)
//    private LocationType type;
//
//    private String country;
//
//    private Double latitude;
//    private Double longitude;
//
//    @ManyToOne
//    @JoinColumn(name = "parentLocationId")
//    private Location parentLocation;
//
//    public enum LocationType {
//        WARD, DISTRICT, CITY, REGION
//    }
//}
