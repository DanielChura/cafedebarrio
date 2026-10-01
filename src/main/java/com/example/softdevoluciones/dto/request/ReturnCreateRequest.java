package com.example.softdevoluciones.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnCreateRequest {

    @NotNull(message = "La compra es obligatoria. Por favor, indica el pedido sobre el cual deseas solicitar la devolución.")
    private UUID orderId;

    @NotBlank(message = "El motivo es obligatorio. Por favor, describe el motivo de la devolución para continuar.")
    @Size(max = 500, message = "El motivo no puede superar los 500 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String reason;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String comment;

    @NotEmpty(message = "Debes incluir al menos un producto en la solicitud. Por favor, selecciona los productos que deseas devolver.")
    @Valid
    private List<ReturnDetailRequest> items;
}
