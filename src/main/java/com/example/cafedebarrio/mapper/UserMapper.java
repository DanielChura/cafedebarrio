package com.example.cafedebarrio.mapper;

import com.example.cafedebarrio.dto.request.UserRequest;
import com.example.cafedebarrio.dto.response.UserResponse;
import com.example.cafedebarrio.entity.User;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }

    public static void updateEntity(User user, UserRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
