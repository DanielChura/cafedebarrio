package com.example.cafedebarrio.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cafedebarrio.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

}
