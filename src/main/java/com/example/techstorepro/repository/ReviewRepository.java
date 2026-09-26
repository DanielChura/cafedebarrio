package com.example.techstorepro.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.techstorepro.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findByProduct_Id(UUID productId, Pageable pageable);

    boolean existsByUser_IdAndProduct_Id(UUID userId, UUID productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") UUID productId);

    long countByProduct_Id(UUID productId);
}
