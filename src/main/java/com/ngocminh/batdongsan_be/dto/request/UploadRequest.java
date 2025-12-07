package com.ngocminh.batdongsan_be.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadRequest {
    String folder;
    @NotNull(message = "File không được để trống")
    MultipartFile file;
}
