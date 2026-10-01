package com.example.softdevoluciones.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.example.softdevoluciones.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public Map<String, Object> upload(MultipartFile file) {
        validateImage(file);
        Map<String, String> options = new HashMap<>();
        options.put("folder", "softdevoluciones");
        options.put("resource_type", "auto");
        try {
            return cloudinary.uploader().upload(file.getBytes(), options);
        } catch (IOException e) {
            throw new BadRequestException(
                    "No fue posible subir la imagen en este momento. Por favor, verifica tu conexión e inténtalo de nuevo con una imagen WEBP válida.");
        }
    }

    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, new HashMap<>());
        } catch (IOException e) {
            throw new BadRequestException(
                    "No fue posible eliminar la imagen anterior del servidor. Puedes continuar, la información del producto quedó guardada correctamente.");
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException(
                    "La imagen del producto es obligatoria. Por favor, adjunta una imagen en formato WEBP para continuar.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("image/webp")) {
            throw new BadRequestException(
                    "El formato de la imagen no es válido. Por favor, adjunta únicamente imágenes en formato WEBP e inténtalo de nuevo.");
        }
    }
}
