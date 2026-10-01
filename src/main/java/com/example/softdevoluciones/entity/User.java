package com.example.softdevoluciones.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.softdevoluciones.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String name;

    @NotBlank(message = "El correo electrónico es obligatorio. Por favor, ingresa tu correo electrónico para continuar.")
    @Size(max = 50, message = "El correo electrónico no puede superar los 50 caracteres. Por favor, verifica el texto ingresado.")
    @Email(message = "El correo electrónico no tiene un formato válido. Por favor, verifica que incluya @ y un dominio válido.")
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria. Por favor, ingresa una contraseña segura para proteger tu cuenta.")
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El rol del usuario es obligatorio. Por favor, verifica la información e inténtalo de nuevo.")
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.CUSTOMER;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String address;

    @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres. Por favor, verifica el número ingresado.")
    @Column(length = 30)
    private String phone;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
