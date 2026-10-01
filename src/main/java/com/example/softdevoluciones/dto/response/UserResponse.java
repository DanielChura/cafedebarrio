package com.example.softdevoluciones.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.softdevoluciones.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private UserRole role;
    private String address;
    private String phone;
    private LocalDateTime createdAt;
}
