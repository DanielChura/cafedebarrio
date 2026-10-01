package com.example.softdevoluciones.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.softdevoluciones.config.JwtUtils;
import com.example.softdevoluciones.dto.request.LoginRequest;
import com.example.softdevoluciones.dto.request.RegisterRequest;
import com.example.softdevoluciones.dto.response.AuthResponse;
import com.example.softdevoluciones.dto.response.UserResponse;
import com.example.softdevoluciones.entity.User;
import com.example.softdevoluciones.enums.UserRole;
import com.example.softdevoluciones.exception.BadRequestException;
import com.example.softdevoluciones.exception.ResourceNotFoundException;
import com.example.softdevoluciones.mapper.UserMapper;
import com.example.softdevoluciones.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException(
                    "El correo electrónico ingresado ya está registrado. Por favor, inicia sesión o utiliza otro correo electrónico para crear la cuenta.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());

        User savedUser = userRepository.save(user);
        String token = jwtUtils.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException(
                        "El correo electrónico o la contraseña son incorrectos. Por favor, verifica los datos ingresados e inténtalo de nuevo."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(
                    "El correo electrónico o la contraseña son incorrectos. Por favor, verifica los datos ingresados e inténtalo de nuevo.");
        }

        String token = jwtUtils.generateToken(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public UserResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el perfil del usuario solicitado. Es posible que la cuenta haya sido eliminada o que el acceso ya no sea válido."));
        return UserMapper.toResponse(user);
    }
}
