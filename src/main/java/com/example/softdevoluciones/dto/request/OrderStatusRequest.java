package com.example.softdevoluciones.dto.request;

import com.example.softdevoluciones.enums.OrderState;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequest {

    @NotNull(message = "El estado del pedido es obligatorio. Por favor, selecciona el nuevo estado que deseas asignar.")
    private OrderState status;

}
