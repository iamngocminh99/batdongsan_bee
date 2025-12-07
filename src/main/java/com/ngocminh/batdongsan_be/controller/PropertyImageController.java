package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.model.PropertyImage;
import com.ngocminh.batdongsan_be.service.PropertyImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/property-image")
@RequiredArgsConstructor
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    @GetMapping("")
    public ResponseEntity<List<PropertyImage>> getAllImages() {
        return ResponseEntity.ok(propertyImageService.getAll());
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<List<PropertyImage>> uploadPropertyImages(
            @PathVariable UUID propertyId,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "type", defaultValue = "IMAGE") PropertyImage.ImageType type,
            @RequestParam(value = "folder", defaultValue = "property_images") String folder
    ) throws IOException {
        List<PropertyImage> propertyImages = propertyImageService.addImages(propertyId, files, folder, type);
        return ResponseEntity.ok(propertyImages);
    }
}
