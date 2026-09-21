package com.example.techstorepro.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.techstorepro.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

}
