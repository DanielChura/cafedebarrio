package com.example.softdevoluciones.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "return_details")
@Getter
@Setter
@NoArgsConstructor
public class ReturnDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "La cantidad a devolver es obligatoria. Por favor, indica cuántas unidades deseas devolver.")
    @Min(value = 1, message = "La cantidad a devolver debe ser al menos 1. Por favor, ingresa un valor válido e inténtalo de nuevo.")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "El importe de la devolución es obligatorio. Por favor, verifica los productos e inténtalo de nuevo.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El importe de la devolución no puede ser negativo. Por favor, verifica los productos e inténtalo de nuevo.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "La solicitud de devolución asociada es obligatoria. Por favor, verifica la información e inténtalo de nuevo.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ReturnRequest request;

    @NotNull(message = "El detalle de la compra es obligatorio. Por favor, indica qué producto del pedido deseas devolver.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private OrderDetail orderDetail;
}
