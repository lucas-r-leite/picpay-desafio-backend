package com.example.desafio_backend.dto;

import com.example.desafio_backend.entity.User;

public record UserDTO( Long id,
    String nome,
    String cpf,
    String email,
    User.UserType type,
    WalletDTO wallet) {

}
