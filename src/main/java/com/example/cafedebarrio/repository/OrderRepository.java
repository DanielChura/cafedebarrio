package com.example.cafedebarrio.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cafedebarrio.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
