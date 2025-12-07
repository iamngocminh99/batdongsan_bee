package com.ngocminh.batdongsan_be.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ngocminh.batdongsan_be.model.Property;
import com.ngocminh.batdongsan_be.model.PropertyImage;
import com.ngocminh.batdongsan_be.repository.PropertyImageRepository;
import com.ngocminh.batdongsan_be.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyImageService {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final Cloudinary cloudinary;

    public List<PropertyImage> getAll() {
        return propertyImageRepository.findAll();
    }

    public List<PropertyImage> addImages(UUID propertyId, MultipartFile[] files, String folder, PropertyImage.ImageType type) throws IOException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        List<PropertyImage> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", folder));

            String imageUrl = uploadResult.get("secure_url").toString();

            PropertyImage propertyImage = PropertyImage.builder()
                    .property(property)
                    .url(imageUrl)
                    .type(type)
                    .build();

            savedImages.add(propertyImageRepository.save(propertyImage));
        }

        return savedImages;
    }
}
