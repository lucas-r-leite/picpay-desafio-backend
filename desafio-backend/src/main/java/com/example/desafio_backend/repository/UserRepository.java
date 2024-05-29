package com.example.desafio_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.desafio_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
