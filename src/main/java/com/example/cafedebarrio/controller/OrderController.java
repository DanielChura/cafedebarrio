package com.example.cafedebarrio.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.cafedebarrio.dto.request.OrderRequest;
import com.example.cafedebarrio.dto.request.OrderStatusRequest;
import com.example.cafedebarrio.dto.response.OrderResponse;
import com.example.cafedebarrio.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Page<OrderResponse> findAll(
            @PageableDefault(size = 10, sort = { "createdAt" }) Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id) {
        return orderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request);
    }
}
