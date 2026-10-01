package com.example.softdevoluciones.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio. Por favor, ingresa un nombre para continuar.")
    @Size(max = 50, message = "El nombre de la categoría no puede superar los 50 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String name;
}
