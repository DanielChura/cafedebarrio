package com.example.cafedebarrio.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(name = "name")
    @Length(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Column(name = "email")
    @Length(min = 1, max = 50, message = "Email must be between 1 and 50 characters")
    @NotNull(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @Column(name = "password")
    @Length(min = 1, max = 50, message = "Password must be between 1 and 50 characters")
    @NotNull(message = "Password is required")
    private String password;

}
