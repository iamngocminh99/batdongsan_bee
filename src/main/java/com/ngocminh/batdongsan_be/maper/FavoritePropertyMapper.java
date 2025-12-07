package com.ngocminh.batdongsan_be.maper;

import com.ngocminh.batdongsan_be.dto.FavoritePropertyDTO;
import com.ngocminh.batdongsan_be.model.FavoriteProperty;
import com.ngocminh.batdongsan_be.model.PropertyImage;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoritePropertyMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "property", target = "property")
    @Mapping(source = "addedAt", target = "addedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    FavoritePropertyDTO toDTO(FavoriteProperty favoriteProperty);

    List<FavoritePropertyDTO> toDTOs(List<FavoriteProperty> favoriteProperties);

    // Mapping User → UserDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    FavoritePropertyDTO.UserDTO toUserDTO(com.ngocminh.batdongsan_be.model.User user);

    // Mapping Property → PropertyDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "saleType", target = "saleType")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "propertyImages", target = "images")
    FavoritePropertyDTO.PropertyDTO toPropertyDTO(com.ngocminh.batdongsan_be.model.Property property);

    // Mapping PropertyImage → PropertyImageDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    FavoritePropertyDTO.PropertyImageDTO toImageDTO(PropertyImage image);
}
