package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, UUID> {
    List<PropertyImage> findByPropertyId(UUID propertyId);
}