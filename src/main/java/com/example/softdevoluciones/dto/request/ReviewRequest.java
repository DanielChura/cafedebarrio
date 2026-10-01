package com.example.softdevoluciones.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    @NotNull(message = "El producto es obligatorio. Por favor, indica el producto que deseas valorar.")
    private UUID productId;

    @NotNull(message = "La calificación es obligatoria. Por favor, selecciona una calificación entre 1 y 5 estrellas.")
    @Min(value = 1, message = "La calificación mínima es 1 estrella. Por favor, selecciona un valor entre 1 y 5.")
    @Max(value = 5, message = "La calificación máxima es 5 estrellas. Por favor, selecciona un valor entre 1 y 5.")
    private Integer rating;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String comment;
}
