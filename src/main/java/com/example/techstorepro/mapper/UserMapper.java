package com.example.techstorepro.mapper;

import com.example.techstorepro.dto.request.UserRequest;
import com.example.techstorepro.dto.response.UserResponse;
import com.example.techstorepro.dto.update.UpdateUserRequest;
import com.example.techstorepro.entity.User;
import com.example.techstorepro.enums.UserRole;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(UserRole.CUSTOMER);
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        return user;
    }

    public static void updateEntity(User user, UpdateUserRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(user.getPassword());
        user.setRole(user.getRole() != null ? user.getRole() : UserRole.CUSTOMER);
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setAddress(user.getAddress());
        response.setPhone(user.getPhone());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
