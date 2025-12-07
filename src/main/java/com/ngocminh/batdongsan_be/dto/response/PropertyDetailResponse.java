package com.ngocminh.batdongsan_be.dto.response;

import com.ngocminh.batdongsan_be.model.Property;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PropertyDetailResponse {
    private UUID id;
    private String title;
    private String description;
    private Double price;

    private int bedrooms;
    private int bathrooms;
    private int livingRooms;
    private int totalRooms;

    private String street;
    private String ward;
    private String district;
    private String city;
    private String fullAddress;

    private Double floorAreaSqft;
    private Double landAreaSqft;
    private Property.Direction direction;

    private Property.PropertyType propertyType;
    private Property.SaleType saleType;
    private Property.PriceType priceType;
    private Property.Status status;

    private UserResponse user;
    private List<String> imageUrls;
}
