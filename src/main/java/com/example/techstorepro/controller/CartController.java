package com.example.techstorepro.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.techstorepro.entity.User;
import com.example.techstorepro.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse getByUserId(@AuthenticationPrincipal User user) {
        return cartService.getByUserId(user.getId());
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse addItem(@AuthenticationPrincipal User user, @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(user.getId(), request);
    }

    @PutMapping("/items")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse updateItem(@AuthenticationPrincipal User user, @Valid @RequestBody CartItemRequest request) {
        return cartService.updateItem(user.getId(), request);
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse removeItem(@AuthenticationPrincipal User user, @PathVariable UUID productId) {
        return cartService.removeItem(user.getId(), productId);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public CartResponse clear(@AuthenticationPrincipal User user) {
        return cartService.clear(user.getId());
    }
}
