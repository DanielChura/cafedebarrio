package com.example.techstorepro.dto.request;

import com.example.techstorepro.enums.OrderState;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderState status;

}
