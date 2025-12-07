package com.ngocminh.batdongsan_be.dto.request;

import com.ngocminh.batdongsan_be.model.Property;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class PropertyCreateRequest {
    private String title;
    private String description;
    private Double price;
    private Property.PriceType priceType;
    private int bedrooms;
    private int bathrooms;
    private int livingRooms;
    private int totalRooms;
    private Property.PropertyType propertyType;
    private Property.SaleType saleType;

    // Địa chỉ chi tiết
    private String street;
    private String ward;
    private String district;
    private String city;
    private String fullAddress;

    private Property.Direction direction;
    private Double latitude;
    private Double longitude;

    private Property.Status status;
    private Double floorAreaSqft;
    private Double landAreaSqft;

    private UUID userId;

    private MultipartFile[] files;
}
