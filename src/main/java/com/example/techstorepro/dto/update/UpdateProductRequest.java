package com.example.techstorepro.dto.update;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;

    @Size(max = 600, message = "Description must be at most 600 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    // Opcional: si es null o vacío se conserva la imagen actual.
    // El input file del navegador no se puede prellenar desde la URL,
    // por eso el update no puede exigirla.
    private MultipartFile image;

    @NotNull(message = "Category is required")
    private UUID categoryId;
}
