package com.example.cafedebarrio.mapper;

import java.util.Collections;
import java.util.List;

import com.example.cafedebarrio.dto.response.OrderItemResponse;
import com.example.cafedebarrio.dto.response.OrderResponse;
import com.example.cafedebarrio.entity.Order;
import com.example.cafedebarrio.entity.OrderDetail;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setPhone(order.getPhone());
        response.setAddress(order.getAddress());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getItems() == null
                ? Collections.emptyList()
                : order.getItems().stream()
                        .map(OrderMapper::toItemResponse)
                        .toList();

        response.setItems(itemResponses);
        return response;
    }

    public static OrderItemResponse toItemResponse(OrderDetail item) {
        OrderItemResponse response = new OrderItemResponse();
        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getId());
        }
        response.setProductName(item.getProductName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());
        return response;
    }
}
