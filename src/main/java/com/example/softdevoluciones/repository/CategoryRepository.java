package com.example.softdevoluciones.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.softdevoluciones.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
