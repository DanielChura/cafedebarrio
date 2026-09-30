package com.example.techstorepro.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.techstorepro.entity.ReturnDetail;

public interface ReturnDetailRepository extends JpaRepository<ReturnDetail, UUID> {

    @Query("SELECT SUM(rd.quantity) FROM ReturnDetail AS rd WHERE rd.orderDetail.id = :orderDetailId")
    Integer findTotalQuantityByOrderDetailId(@Param("orderDetailId") UUID orderDetailId);
}
