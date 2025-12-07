package com.ngocminh.batdongsan_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritePropertyDTO {
    private UUID id;
    private UserDTO user;
    private PropertyDTO property;
    private String addedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDTO {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PropertyDTO {
        private UUID id;
        private String title;
        private Double price;
        private String saleType;
        private String status;
        private List<PropertyImageDTO> images;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PropertyImageDTO {
        private UUID id;
        private String url;
    }
}
