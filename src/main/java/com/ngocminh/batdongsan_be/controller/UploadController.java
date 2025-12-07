package com.ngocminh.batdongsan_be.controller;
import com.ngocminh.batdongsan_be.dto.request.UploadRequest;
import com.ngocminh.batdongsan_be.service.CloudinaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(
            @Valid @ModelAttribute UploadRequest request,
            BindingResult bindingResult
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMsg);
        }
        String folder = (request.getFolder() == null || request.getFolder().trim().isEmpty())
                ? "images"
                : request.getFolder();

        String imageUrl = cloudinaryService.getImageUrlAfterUpload(request.getFile(), folder);
        return ResponseEntity.ok(imageUrl);
    }

}

