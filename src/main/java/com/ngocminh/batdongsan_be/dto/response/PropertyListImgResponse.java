package com.ngocminh.batdongsan_be.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PropertyListImgResponse {
    private String id;
    private String title;
    private String description;
    private Double price;
    private String priceType;
    private int bedrooms;
    private int bathrooms;
    private int livingRooms;
    private int totalRooms;
    private String propertyType;
    private String saleType;
    private String street;
    private String area;
    private String fullAddress;
    private String status;
    private Double floorAreaSqft;
    private Double landAreaSqft;
    private String createdAt;
    private List<String> images;
}
