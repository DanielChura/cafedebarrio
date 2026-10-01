package com.example.softdevoluciones.dto.response;

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
public class AuthResponse {

    private String token;
    private UUID id;
    private String name;
    private String email;
    private UserRole role;

}
