package com.example.softdevoluciones.dto.request;

import com.example.softdevoluciones.enums.ReturnStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnStatusRequest {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado de la devolución es obligatorio. Por favor, selecciona el nuevo estado de la solicitud.")
    private ReturnStatus status;

    @Size(max = 500, message = "La nota del operador no puede superar los 500 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    @NotNull(message = "La nota del operador es obligatoria. Por favor, agrega un comentario que explique la decisión tomada.")
    private String operatorNote;
}
