package com.example.cafedebarrio.dto.request;

import com.example.cafedebarrio.enums.OrderState;

import jakarta.validation.constraints.NotNull;

public class OrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderState status;

    public OrderState getStatus() {
        return status;
    }

    public void setStatus(OrderState status) {
        this.status = status;
    }
}
