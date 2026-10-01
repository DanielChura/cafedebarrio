package com.example.softdevoluciones.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre de la categoría es obligatorio. Por favor, ingresa un nombre para continuar.")
    @Size(max = 50, message = "El nombre de la categoría no puede superar los 50 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    @Column(nullable = false, length = 50)
    private String name;
}
