package com.example.softdevoluciones.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "product_id" }))
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "El usuario de la reseña es obligatorio. Por favor, verifica la sesión e inténtalo de nuevo.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @NotNull(message = "El producto valorado es obligatorio. Por favor, indica el producto que deseas valorar.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @NotNull(message = "La calificación es obligatoria. Por favor, selecciona una calificación entre 1 y 5 estrellas.")
    @Min(value = 1, message = "La calificación mínima es 1 estrella. Por favor, selecciona un valor entre 1 y 5.")
    @Max(value = 5, message = "La calificación máxima es 5 estrellas. Por favor, selecciona un valor entre 1 y 5.")
    @Column(nullable = false)
    private Integer rating;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    @Column(length = 500)
    private String comment;

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
