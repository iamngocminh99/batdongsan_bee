package com.ngocminh.batdongsan_be.dto.response;

import com.ngocminh.batdongsan_be.model.Property;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PropertyResponse {
    private UUID id;
    private String title;
    private String description;
    private Double price;

    private int bedrooms;
    private int bathrooms;
    private int livingRooms;
    private int totalRooms;

    private String street;     // Đường
    private String ward;       // Phường / Xã
    private String district;   // Quận / Huyện
    private String city;       // Thành phố / Tỉnh
    private String fullAddress; // Địa chỉ đầy đủ

    private Double latitude;   // Vĩ độ (Lat)
    private Double longitude;  // Kinh độ (Lng)

    private String propertyType;   // APARTMENT / HOUSE / LAND
    private String saleType;       // SALE / RENT
    private String priceType;      // TOTAL / MONTHLY / WEEKLY
    private String status;         // PUBLISHED / SOLD / RENTED...


    private Double floorAreaSqft;
    private Double landAreaSqft;

    private String direction;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
