package com.example.techstorepro.dto.request;

import com.example.techstorepro.enums.ReturnStatus;

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
    @NotNull(message = "El estado es obligatorio")
    private ReturnStatus status;

    @Size(max = 500, message = "La observación debe tener como máximo 500 caracteres")
    private String operatorNote;
}
