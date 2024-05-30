package com.example.desafio_backend.dto;

public record AuthorizationResponseDTO(String status,  Data data) {

public static record Data(boolean authorization) {}

}
