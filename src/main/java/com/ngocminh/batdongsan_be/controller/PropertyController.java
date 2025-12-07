package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.dto.request.PropertyCreateRequest;
import com.ngocminh.batdongsan_be.dto.response.ChatPartnerResponse;
import com.ngocminh.batdongsan_be.dto.response.PropertyDetailResponse;
import com.ngocminh.batdongsan_be.dto.response.PropertyResponse;
import com.ngocminh.batdongsan_be.model.Property;
import com.ngocminh.batdongsan_be.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<?> create(@ModelAttribute PropertyCreateRequest request) {
        try {
            propertyService.create(request);
            return ResponseEntity.ok("Property created successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo bài đăng!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @ModelAttribute PropertyCreateRequest request
    ) {
        try {
            propertyService.updateWithFiles(id, request);
            return ResponseEntity.ok("Property updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi cập nhật bài đăng!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<PropertyDetailResponse> getDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(propertyService.getPropertyDetails(id));
    }

    @GetMapping
    public ResponseEntity<Page<PropertyResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Property.PropertyType propertyType,
            @RequestParam(required = false) Property.SaleType saleType,
            @RequestParam(required = false) Property.Direction direction,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String ward,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String fullAddress,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(propertyService.search(
                title, propertyType, saleType, direction,
                minPrice, maxPrice, minBedrooms, maxBedrooms,
                district, ward, street, city, fullAddress,
                page, size
        ));
    }


    @GetMapping("/user/{userId}/search")
    public ResponseEntity<Page<PropertyResponse>> searchByUser(
            @PathVariable UUID userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Property.PropertyType propertyType,
            @RequestParam(required = false) Property.SaleType saleType,
            @RequestParam(required = false) Property.Direction direction,
            @RequestParam(required = false) Property.Status status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(propertyService.searchByUser(
                userId, title, propertyType, saleType, direction,status,
                minPrice, maxPrice, minBedrooms, maxBedrooms,
                address, page, size
        ));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PropertyResponse>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(propertyService.getPropertiesByUser(userId));
    }

    @GetMapping("/{propertyId}/owner")
    public ResponseEntity<ChatPartnerResponse> getPropertyOwner(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(propertyService.getOwnerByPropertyId(propertyId));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countProperties() {
        long total = propertyService.countProperties();
        return ResponseEntity.ok(Map.of("totalProperties", total));
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllPropertyTypes() {
        List<String> types = Arrays.stream(Property.PropertyType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(types);
    }

}
