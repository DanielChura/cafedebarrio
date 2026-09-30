package com.example.techstorepro.entity;

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

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El importe debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "La solicitud es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ReturnRequest request;

    @NotNull(message = "El detalle de compra es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private OrderDetail orderDetail;
}
