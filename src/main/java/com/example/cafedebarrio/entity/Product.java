package com.example.cafedebarrio.entity;

import java.sql.Date;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID productId;

    @Column(name = "name")
    @Length(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "description")
    @Length(min = 1, max = 200, message = "Description must be between 1 and 50 characters")
    @NotNull(message = "Description is required")
    private String description;

    @Column(name = "price")
    @Length(min = 1, max = 10, message = "Price must be between 1 and 10 characters")
    @NotNull(message = "Price is required")
    private Double price;

    @Column(name = "stock")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    @NotNull(message = "Stock is required")
    private Integer stock;

    @Column(name = "image_url")
    @Length(min = 1, max = 200, message = "Image URL must be between 1 and 200 characters")
    @NotNull(message = "Image URL is required")
    private String imageUrl;

    @Column(name = "is_active")
    @NotNull(message = "Active is required")
    private Boolean isActive;

    @Column(name = "category_id")
    @NotNull(message = "Category is required")
    private String categoryId;

    @Column(name = "created_at")
    @NotNull(message = "Created at is required")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @NotNull(message = "Updated at is required")
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Category category;
}
