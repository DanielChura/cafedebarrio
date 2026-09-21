package com.example.techstorepro.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techstorepro.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
