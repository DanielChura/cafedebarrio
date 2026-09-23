package com.example.techstorepro.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.example.techstorepro.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public Map<String, Object> upload(MultipartFile file) {
        validateImage(file);
        Map<String, String> options = new HashMap<>();
        options.put("folder", "techstorepro");
        options.put("resource_type", "auto");
        try {
            return cloudinary.uploader().upload(file.getBytes(), options);
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image");
        }
    }

    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, new HashMap<>());
        } catch (IOException e) {
            throw new BadRequestException("Failed to delete image");
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image is required");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("image/webp")) {
            throw new BadRequestException("Only WEBP images are allowed");
        }
    }
}
