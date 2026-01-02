package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.dto.request.PropertyCreateRequest;
import com.ngocminh.batdongsan_be.dto.response.*;
import com.ngocminh.batdongsan_be.model.*;
import com.ngocminh.batdongsan_be.repository.PropertyRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    public void create(PropertyCreateRequest request) throws IOException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check Agent plan
        if (user.getRole() == User.Role.AGENT && user.getAgent() != null) {
            Agent agent = user.getAgent();
            LocalDateTime now = LocalDateTime.now();

            if (agent.getPlanEndDate() == null || now.isAfter(agent.getPlanEndDate())) {
                throw new IllegalStateException(
                        "Gói " + agent.getPlanName() + " của bạn đã hết hạn vào " +
                                (agent.getPlanEndDate() != null ? agent.getPlanEndDate().toLocalDate() : "N/A")
                                + ". Vui lòng gia hạn để tiếp tục đăng tin!"
                );
            }

            long currentCount = propertyRepository.countByUser_Agent_Id(agent.getId());
            int maxAllowed = agent.getMaxProperties() != null ? agent.getMaxProperties() : 3;

            if (currentCount >= maxAllowed) {
                throw new IllegalStateException(
                        "Bạn đã đạt giới hạn " + maxAllowed + " bài đăng cho gói " + agent.getPlanName()
                                + ". Vui lòng nâng cấp hoặc xóa bớt bài cũ!"
                );
            }
        }

        // Build fullAddress nếu trống
        String fullAddress = buildFullAddress(request);

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .priceType(request.getPriceType())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .livingRooms(request.getLivingRooms())
                .totalRooms(request.getTotalRooms())
                .propertyType(request.getPropertyType())
                .saleType(request.getSaleType())
                .direction(request.getDirection())
                .street(request.getStreet())
                .ward(request.getWard())
                .district(request.getDistrict())
                .city(request.getCity())
                .fullAddress(fullAddress)
                .status(request.getStatus())
                .floorAreaSqft(request.getFloorAreaSqft())
                .landAreaSqft(request.getLandAreaSqft())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .user(user)
                .favoriteCount(0)
                .build();

        Property saved = propertyRepository.save(property);

        // Upload ảnh
        if (request.getFiles() != null && request.getFiles().length > 0) {
            List<PropertyImage> images = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (file != null && !file.isEmpty()) {
                    String url = cloudinaryService.getImageUrlAfterUpload(file, "properties");
                    PropertyImage image = PropertyImage.builder()
                            .property(saved)
                            .url(url)
                            .type(PropertyImage.ImageType.IMAGE)
                            .build();
                    images.add(image);
                }
            }
            saved.setPropertyImages(images);
            propertyRepository.save(saved);
        }
    }

    private String buildFullAddress(PropertyCreateRequest req) {
        if (req.getFullAddress() != null && !req.getFullAddress().isBlank()) return req.getFullAddress();

        StringBuilder sb = new StringBuilder();
        if (req.getStreet() != null && !req.getStreet().isBlank()) sb.append(req.getStreet());
        if (req.getWard() != null && !req.getWard().isBlank()) sb.append(", ").append(req.getWard());
        if (req.getDistrict() != null && !req.getDistrict().isBlank()) sb.append(", ").append(req.getDistrict());
        if (req.getCity() != null && !req.getCity().isBlank()) sb.append(", ").append(req.getCity());
        return sb.toString();
    }

    @Transactional
    public void updateWithFiles(UUID id, PropertyCreateRequest request) throws IOException {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setPriceType(request.getPriceType());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setLivingRooms(request.getLivingRooms());
        property.setTotalRooms(request.getTotalRooms());
        property.setPropertyType(request.getPropertyType());
        property.setSaleType(request.getSaleType());
        property.setStreet(request.getStreet());
        property.setWard(request.getWard());
        property.setDistrict(request.getDistrict());
        property.setCity(request.getCity());
        property.setFullAddress(buildFullAddress(request));
        property.setStatus(request.getStatus());
        property.setFloorAreaSqft(request.getFloorAreaSqft());
        property.setLandAreaSqft(request.getLandAreaSqft());
        property.setDirection(request.getDirection());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());

        if (request.getFiles() != null && request.getFiles().length > 0) {

            if (property.getPropertyImages() == null) {
                property.setPropertyImages(new ArrayList<>());
            }
            for (PropertyImage img : property.getPropertyImages()) {
                img.setProperty(null);
            }
            property.getPropertyImages().clear();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String url = cloudinaryService.getImageUrlAfterUpload(file, "properties");

                    PropertyImage image = PropertyImage.builder()
                            .property(property)
                            .url(url)
                            .type(PropertyImage.ImageType.IMAGE)
                            .build();

                    property.getPropertyImages().add(image);
                }
            }
        }
    }

    public Page<PropertyResponse> search(
            String title, Property.PropertyType propertyType, Property.SaleType saleType, Property.Direction direction,
            Double minPrice, Double maxPrice,
            Integer minBedrooms, Integer maxBedrooms,
            String district, String ward, String street, String city, String fullAddress,
            int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Property> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank())
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            if (district != null && !district.isBlank())
                predicates.add(cb.like(cb.lower(root.get("district")), "%" + district.toLowerCase() + "%"));
            if (ward != null && !ward.isBlank())
                predicates.add(cb.like(cb.lower(root.get("ward")), "%" + ward.toLowerCase() + "%"));
            if (street != null && !street.isBlank())
                predicates.add(cb.like(cb.lower(root.get("street")), "%" + street.toLowerCase() + "%"));
            if (city != null && !city.isBlank())
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            if (fullAddress != null && !fullAddress.isBlank())
                predicates.add(cb.like(cb.lower(root.get("fullAddress")), "%" + fullAddress.toLowerCase() + "%"));
            if (propertyType != null)
                predicates.add(cb.equal(root.get("propertyType"), propertyType));
            if (saleType != null)
                predicates.add(cb.equal(root.get("saleType"), saleType));
            if (direction != null)
                predicates.add(cb.equal(root.get("direction"), direction));
            if (minPrice != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            if (minBedrooms != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("bedrooms"), minBedrooms));
            if (maxBedrooms != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("bedrooms"), maxBedrooms));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Property> properties = propertyRepository.findAll(spec, pageable);
        return properties.map(this::toResponse);
    }

    public Page<PropertyResponse> searchByUser(
            UUID userId, String title,
            Property.PropertyType propertyType, Property.SaleType saleType, Property.Direction direction, Property.Status status,
            Double minPrice, Double maxPrice, Integer minBedrooms, Integer maxBedrooms,
            String address, int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Property> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (title != null && !title.isBlank())
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            if (propertyType != null)
                predicates.add(cb.equal(root.get("propertyType"), propertyType));
            if (saleType != null)
                predicates.add(cb.equal(root.get("saleType"), saleType));
            if (direction != null)
                predicates.add(cb.equal(root.get("direction"), direction));
            if (status != null)
                predicates.add(cb.equal(root.get("status"), status));
            if (minPrice != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            if (minBedrooms != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("bedrooms"), minBedrooms));
            if (maxBedrooms != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("bedrooms"), maxBedrooms));
            if (address != null && !address.isBlank()) {
                String kw = "%" + address.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("street")), kw),
                        cb.like(cb.lower(root.get("ward")), kw),
                        cb.like(cb.lower(root.get("district")), kw),
                        cb.like(cb.lower(root.get("city")), kw),
                        cb.like(cb.lower(root.get("fullAddress")), kw)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Property> properties = propertyRepository.findAll(spec, pageable);
        return properties.map(this::toResponse);
    }


    public Optional<Property> getById(UUID id) {
        return propertyRepository.findById(id);
    }

    public void delete(UUID id) {
        propertyRepository.deleteById(id);
    }

    public List<PropertyResponse> getPropertiesByUser(UUID userId) {
        return propertyRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PropertyDetailResponse getPropertyDetails(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        List<String> imageUrls = property.getPropertyImages() != null ?
                property.getPropertyImages().stream()
                        .filter(img -> img.getType() == PropertyImage.ImageType.IMAGE)
                        .map(PropertyImage::getUrl)
                        .toList()
                : List.of();

        User user = property.getUser();
        UserResponse userResponse = null;

        if (user != null) {
            userResponse = UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhone())
                    .role(user.getRole().name())
                    .build();
        }

        return PropertyDetailResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price(property.getPrice())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .livingRooms(property.getLivingRooms())
                .totalRooms(property.getTotalRooms())
                .street(property.getStreet())
                .ward(property.getWard())
                .direction(property.getDirection())
                .district(property.getDistrict())
                .city(property.getCity())
                .fullAddress(property.getFullAddress())
                .status(property.getStatus())
                .propertyType(property.getPropertyType())
                .saleType(property.getSaleType())
                .priceType(property.getPriceType())
                .floorAreaSqft(property.getFloorAreaSqft())
                .landAreaSqft(property.getLandAreaSqft())
                .longitude(property.getLongitude())
                .latitude(property.getLatitude())
                .user(userResponse)
                .imageUrls(imageUrls)
                .build();
    }

    private PropertyResponse toResponse(Property p) {
        String firstImageUrl = null;
        if (p.getPropertyImages() != null && !p.getPropertyImages().isEmpty()) {
            firstImageUrl = p.getPropertyImages().stream()
                    .filter(img -> img.getType() == PropertyImage.ImageType.IMAGE)
                    .map(PropertyImage::getUrl)
                    .findFirst()
                    .orElse(p.getPropertyImages().get(0).getUrl());
        }

        return PropertyResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .price(p.getPrice())
                .priceType(p.getPriceType() != null ? p.getPriceType().name() : null)
                .propertyType(p.getPropertyType() != null ? p.getPropertyType().name() : null)
                .saleType(p.getSaleType() != null ? p.getSaleType().name() : null)
                .direction(p.getDirection() != null ? p.getDirection().name() : null)
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .bedrooms(p.getBedrooms())
                .bathrooms(p.getBathrooms())
                .livingRooms(p.getLivingRooms())
                .totalRooms(p.getTotalRooms())
                .street(p.getStreet())
                .ward(p.getWard())
                .district(p.getDistrict())
                .city(p.getCity())
                .fullAddress(p.getFullAddress())
                .latitude(p.getLatitude())
                .longitude(p.getLongitude())
                .floorAreaSqft(p.getFloorAreaSqft())
                .landAreaSqft(p.getLandAreaSqft())
                .imageUrl(firstImageUrl)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    public ChatPartnerResponse getOwnerByPropertyId(UUID propertyId) {
        User user = propertyRepository.findOwnerByPropertyId(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found or has no owner"));

        ChatPartnerResponse dto = new ChatPartnerResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }

    public long countProperties() {
        return propertyRepository.count();
    }

    public Map<String, Object> getPropertyStats(Integer day, Integer month, Integer year) {

        LocalDate start;
        LocalDate end;

        if (day != null && month != null && year != null) {
            start = LocalDate.of(year, month, day);
            end = start.plusDays(1);
        } else if (month != null && year != null) {
            start = LocalDate.of(year, month, 1);
            end = start.plusMonths(1);
        } else if (year != null) {
            start = LocalDate.of(year, 1, 1);
            end = start.plusYears(1);
        } else {
            LocalDate now = LocalDate.now();
            start = now.withDayOfMonth(1);
            end = start.plusMonths(1);
        }

        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt = end.atStartOfDay();

        long totalProperties =
                propertyRepository.countByCreatedAtBetween(startDt, endDt);

        long saleCount =
                propertyRepository.countBySaleTypeAndCreatedAtBetween(
                        Property.SaleType.SALE, startDt, endDt);

        long rentCount =
                propertyRepository.countBySaleTypeAndCreatedAtBetween(
                        Property.SaleType.RENT, startDt, endDt);

        long published =
                propertyRepository.countByStatusAndCreatedAtBetween(
                        Property.Status.PUBLISHED, startDt, endDt);

        long pending =
                propertyRepository.countByStatusAndCreatedAtBetween(
                        Property.Status.PENDING, startDt, endDt);

        return Map.of(
                "totalProperties", totalProperties,
                "sale", saleCount,
                "rent", rentCount,
                "published", published,
                "pending", pending
        );
    }


}
