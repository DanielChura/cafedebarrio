package com.example.softdevoluciones.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.softdevoluciones.enums.OrderState;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "El usuario del pedido es obligatorio. Por favor, verifica la sesión e inténtalo de nuevo.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @NotBlank(message = "El teléfono es obligatorio. Por favor, ingresa un número de contacto para coordinar la entrega.")
    @Size(max = 50, message = "El teléfono no puede superar los 50 caracteres. Por favor, verifica el número ingresado.")
    @Column(nullable = false, length = 50)
    private String phone;

    @NotBlank(message = "La dirección es obligatoria. Por favor, ingresa la dirección de entrega del pedido.")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    @Column(nullable = false, length = 200)
    private String address;

    @NotNull(message = "El estado del pedido es obligatorio. Por favor, indica el estado correspondiente para continuar.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderState status = OrderState.PENDING;

    @NotNull(message = "El total del pedido es obligatorio. Por favor, verifica los productos e inténtalo de nuevo.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total del pedido no puede ser negativo. Por favor, verifica los productos e inténtalo de nuevo.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
