package com.example.cafedebarrio.dto.response;

import java.util.UUID;

import com.example.cafedebarrio.enums.UserRole;

public class AuthResponse {

    private String token;
    private UUID id;
    private String name;
    private String email;
    private UserRole role;

    public AuthResponse() {
    }

    public AuthResponse(String token, UUID id, String name, String email, UserRole role) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

}
