package com.example.techstorepro.dto.request;

import com.example.techstorepro.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must be at most 100 characters")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(max = 100, message = "Password must be at most 100 characters")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;

}
