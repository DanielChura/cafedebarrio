package com.example.softdevoluciones.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnDetailRequest {

    @NotNull(message = "El detalle de la compra es obligatorio. Por favor, indica qué producto del pedido deseas devolver.")
    private UUID orderDetailId;

    @NotNull(message = "La cantidad es obligatoria. Por favor, indica cuántas unidades deseas devolver.")
    @Min(value = 1, message = "La cantidad a devolver debe ser al menos 1. Por favor, ingresa un valor válido e inténtalo de nuevo.")
    private Integer quantity;
}
