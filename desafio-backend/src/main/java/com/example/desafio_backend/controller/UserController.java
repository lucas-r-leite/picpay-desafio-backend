package com.example.desafio_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.desafio_backend.dto.UserDTO;
import com.example.desafio_backend.entity.User;
import com.example.desafio_backend.repository.UserRepository;
import com.example.desafio_backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Operation(summary = "Create new user")
    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User body) {
        
        Optional<User> user = this.userRepository.findByEmail(body.getEmail());
        
        if(user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User createdUser = userService.saveUser(body);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
