package com.example.techstorepro.dto.request;

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

    @NotNull(message = "La compra es obligatoria")
    private UUID orderId;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 500, message = "El motivo debe tener como máximo 500 caracteres")
    private String reason;

    @Size(max = 500, message = "El comentario debe tener como máximo 500 caracteres")
    private String comment;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    @Valid
    private List<ReturnDetailRequest> items;
}
