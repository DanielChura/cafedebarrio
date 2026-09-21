package com.example.techstorepro.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.techstorepro.enums.OrderState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private UUID userId;
    private String phone;
    private String address;
    private OrderState status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
