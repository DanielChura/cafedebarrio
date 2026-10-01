package com.example.softdevoluciones.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = { "cart_id", "product_id" }))
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "El carrito asociado es obligatorio. Por favor, verifica la sesión e inténtalo de nuevo.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cart cart;

    @NotNull(message = "El producto es obligatorio. Por favor, selecciona el producto que deseas agregar al carrito.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @NotNull(message = "La cantidad es obligatoria. Por favor, indica cuántas unidades deseas agregar.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1. Por favor, ingresa un valor válido e inténtalo de nuevo.")
    @Column(nullable = false)
    private Integer quantity;
}
