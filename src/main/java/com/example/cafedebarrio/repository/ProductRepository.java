package com.example.cafedebarrio.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cafedebarrio.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByIsActiveTrue();

    List<Product> findByCategoryId(UUID categoryId);

    @Query("""
            SELECT p FROM Product p
            WHERE (:categoryId IS NULL OR p.category.id = :categoryId)
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:onlyActive = false OR p.isActive = true)
            """)
    List<Product> findByFilters(
            @Param("categoryId") UUID categoryId,
            @Param("name") String name,
            @Param("onlyActive") boolean onlyActive);
}
