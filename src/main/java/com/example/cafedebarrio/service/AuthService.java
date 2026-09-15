package com.example.cafedebarrio.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cafedebarrio.config.JwtUtils;
import com.example.cafedebarrio.dto.request.LoginRequest;
import com.example.cafedebarrio.dto.request.RegisterRequest;
import com.example.cafedebarrio.dto.response.AuthResponse;
import com.example.cafedebarrio.dto.response.UserResponse;
import com.example.cafedebarrio.entity.User;
import com.example.cafedebarrio.enums.UserRole;
import com.example.cafedebarrio.exception.BadRequestException;
import com.example.cafedebarrio.exception.ResourceNotFoundException;
import com.example.cafedebarrio.mapper.UserMapper;
import com.example.cafedebarrio.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);

        User savedUser = userRepository.save(user);
        String token = jwtUtils.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getUserId(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user);

        return new AuthResponse(token, user.getUserId(), user.getName(), user.getEmail(), user.getRole());
    }

    public UserResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserMapper.toResponse(user);
    }
}
