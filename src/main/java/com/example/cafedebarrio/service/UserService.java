package com.example.cafedebarrio.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cafedebarrio.dto.request.UserRequest;
import com.example.cafedebarrio.dto.response.UserResponse;
import com.example.cafedebarrio.entity.User;
import com.example.cafedebarrio.exception.BadRequestException;
import com.example.cafedebarrio.exception.ResourceNotFoundException;
import com.example.cafedebarrio.mapper.UserMapper;
import com.example.cafedebarrio.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(u -> UserMapper.toResponse(u));
    }

    public UserResponse findById(UUID id) {
        return UserMapper.toResponse(findUserByIdOrThrow(id));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(UUID id, UserRequest request) {
        User user = findUserByIdOrThrow(id);
        UserMapper.updateEntity(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
    }

    private User findUserByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}