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
public class OrderRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Phone is required")
    @Size(max = 50, message = "Phone must be at most 50 characters")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must be at most 200 characters")
    private String address;

    @NotNull(message = "Items are required")
    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequest> items;

}