package com.example.softdevoluciones.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.softdevoluciones.entity.ReturnRequest;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, UUID> {

    List<ReturnRequest> findByOrderId(UUID orderId);

    Page<ReturnRequest> findByUser_Id(UUID userId, Pageable pageable);
}
