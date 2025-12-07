package com.ngocminh.batdongsan_be.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    private int bedrooms;
    private int bathrooms;
    private int livingRooms;
    private int totalRooms;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;


    private String street;     // Đường
    private String ward;       // Phường / Xã
    private String district;   // Quận / Huyện
    private String city;       // Thành phố / Tỉnh
    private String fullAddress; // Địa chỉ đầy đủ

    private Double latitude;   // Vĩ độ (Lat)
    private Double longitude;  // Kinh độ (Lng)

    private String floor;
    private String yearBuilt;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Double floorAreaSqft;
    private Double landAreaSqft;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private int favoriteCount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "locationId")
//    private Location location;
//
//    @ManyToOne
//    @JoinColumn(name = "approvedBy")
//    private User approvedBy;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PropertyImage> propertyImages;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FavoriteProperty> favorites;

    public enum PriceType { TOTAL, MONTHLY, WEEKLY }
    public enum PropertyType { APARTMENT, HOUSE, LAND }
    public enum SaleType { SALE, RENT }
    public enum Status { DRAFT, PENDING, PUBLISHED, SOLD, RENTED }
    public enum Direction {
        NORTH, NORTHEAST, EAST, SOUTHEAST,
        SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

}

