package com.example.cafedebarrio.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cafedebarrio.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByOrderByCreatedAtDesc();
}
