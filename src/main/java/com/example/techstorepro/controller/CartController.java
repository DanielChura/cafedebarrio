package com.example.techstorepro.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.techstorepro.dto.request.CartItemRequest;
import com.example.techstorepro.dto.response.CartResponse;
import com.example.techstorepro.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart/{userId}")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse getByUserId(@PathVariable UUID userId) {
        return cartService.getByUserId(userId);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse addItem(@PathVariable UUID userId, @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(userId, request);
    }

    @PutMapping("/items")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse updateItem(@PathVariable UUID userId, @Valid @RequestBody CartItemRequest request) {
        return cartService.updateItem(userId, request);
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse removeItem(@PathVariable UUID userId, @PathVariable UUID productId) {
        return cartService.removeItem(userId, productId);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse clear(@PathVariable UUID userId) {
        return cartService.clear(userId);
    }
}
