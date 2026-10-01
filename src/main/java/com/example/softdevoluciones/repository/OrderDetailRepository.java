package com.example.softdevoluciones.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.softdevoluciones.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    boolean existsByOrder_User_IdAndProduct_Id(UUID userId, UUID productId);
}
