package com.example.techstorepro.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must be at most 100 characters")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Size(max = 30, message = "Phone must be at most 30 characters")
    private String phone;

}
