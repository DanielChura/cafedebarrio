package com.example.softdevoluciones.dto.request;

import com.example.softdevoluciones.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio. Por favor, ingresa tu nombre para continuar.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String name;

    @NotBlank(message = "El correo electrónico es obligatorio. Por favor, ingresa tu correo electrónico para continuar.")
    @Size(max = 100, message = "El correo electrónico no puede superar los 100 caracteres. Por favor, verifica el texto ingresado.")
    @Email(message = "El correo electrónico no tiene un formato válido. Por favor, verifica que incluya @ y un dominio válido.")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria. Por favor, ingresa una contraseña para continuar.")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres. Por favor, ajusta la longitud e inténtalo de nuevo.")
    private String password;

    private UserRole role;

    @Size(max = 255, message = "La dirección no puede superar los 255 caracteres. Por favor, acorta el texto e inténtalo de nuevo.")
    private String address;

    @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres. Por favor, verifica el número ingresado.")
    private String phone;
}
