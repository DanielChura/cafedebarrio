package com.example.softdevoluciones.dto.update;

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

    @NotBlank(message = "El nombre del producto es obligatorio. Por favor, ingresa un nombre para continuar.")
    @Size(max = 200, message = "El nombre del producto no puede superar los 200 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String name;

    @Size(max = 600, message = "La descripción no puede superar los 600 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String description;

    @NotNull(message = "El precio es obligatorio. Por favor, ingresa el precio del producto para continuar.")
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0. Por favor, ingresa un valor válido e inténtalo de nuevo.")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio. Por favor, indica la cantidad disponible del producto.")
    @Min(value = 0, message = "El stock no puede ser negativo. Por favor, ingresa un valor igual o mayor a 0.")
    private Integer stock;

    // Opcional: si es null o vacío se conserva la imagen actual.
    // El input file del navegador no se puede prellenar desde la URL,
    // por eso el update no puede exigirla.
    private MultipartFile image;

    @NotNull(message = "La categoría es obligatoria. Por favor, selecciona la categoría a la que pertenece el producto.")
    private UUID categoryId;
}
