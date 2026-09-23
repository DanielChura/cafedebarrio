package com.example.techstorepro.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.techstorepro.dto.request.OrderRequest;
import com.example.techstorepro.dto.request.OrderStatusRequest;
import com.example.techstorepro.dto.response.OrderResponse;
import com.example.techstorepro.entity.User;
import com.example.techstorepro.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public Page<OrderResponse> findAll(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10, sort = { "createdAt" }, direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.findAllFor(user, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public OrderResponse findById(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return orderService.findByIdFor(user, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public OrderResponse create(@AuthenticationPrincipal User user, @Valid @RequestBody OrderRequest request) {
        return orderService.create(user.getId(), request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request);
    }
}
