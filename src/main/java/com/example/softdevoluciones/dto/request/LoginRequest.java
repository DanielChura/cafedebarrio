package com.example.softdevoluciones.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "El correo electrónico es obligatorio. Por favor, ingresa tu correo electrónico para iniciar sesión.")
    @Email(message = "El correo electrónico no tiene un formato válido. Por favor, verifica que incluya @ y un dominio válido.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria. Por favor, ingresa tu contraseña para iniciar sesión.")
    private String password;
}
