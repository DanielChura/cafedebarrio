package com.example.techstorepro.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String message, int status, Map<String, String> errors) {

    public ApiError(String message, int status) {
        this(message, status, null);
    }
}
