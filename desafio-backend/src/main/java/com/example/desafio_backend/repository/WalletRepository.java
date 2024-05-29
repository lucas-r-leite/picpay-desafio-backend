package com.example.desafio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.desafio_backend.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
