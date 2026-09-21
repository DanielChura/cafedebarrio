package com.example.techstorepro.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.techstorepro.enums.UserRole;

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
    private LocalDateTime createdAt;
}
