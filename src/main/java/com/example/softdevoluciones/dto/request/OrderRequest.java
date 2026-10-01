package com.example.softdevoluciones.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "El teléfono es obligatorio. Por favor, ingresa un número de contacto para coordinar la entrega.")
    @Size(max = 50, message = "El teléfono no puede superar los 50 caracteres. Por favor, verifica el número ingresado.")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria. Por favor, ingresa la dirección de entrega del pedido.")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String address;
}
