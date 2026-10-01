package com.example.softdevoluciones.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {

    @NotNull(message = "El producto es obligatorio. Por favor, selecciona el producto que deseas agregar al carrito.")
    private UUID productId;

    @NotNull(message = "La cantidad es obligatoria. Por favor, indica cuántas unidades deseas agregar.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1. Por favor, ingresa un valor válido e inténtalo de nuevo.")
    private Integer quantity;
}
