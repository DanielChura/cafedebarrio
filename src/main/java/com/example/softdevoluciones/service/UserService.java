package com.example.softdevoluciones.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.softdevoluciones.dto.request.UserRequest;
import com.example.softdevoluciones.dto.response.UserResponse;
import com.example.softdevoluciones.dto.update.UpdateUserRequest;
import com.example.softdevoluciones.entity.User;
import com.example.softdevoluciones.exception.BadRequestException;
import com.example.softdevoluciones.exception.ResourceNotFoundException;
import com.example.softdevoluciones.mapper.UserMapper;
import com.example.softdevoluciones.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(u -> UserMapper.toResponse(u));
    }

    public UserResponse findById(UUID id) {
        return UserMapper.toResponse(getUser(id));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException(
                    "El correo electrónico ingresado ya está registrado. Por favor, utiliza otro correo electrónico para crear la cuenta.");
        }
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = getUser(id);
        UserMapper.updateEntity(user, request);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = getUser(id);
        userRepository.delete(user);
    }

    private User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario solicitado no existe o ya fue eliminado. Por favor, verifica la información e inténtalo de nuevo."));
    }
}