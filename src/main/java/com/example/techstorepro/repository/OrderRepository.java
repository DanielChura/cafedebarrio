package com.example.techstorepro.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techstorepro.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
