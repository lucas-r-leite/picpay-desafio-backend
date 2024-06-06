package com.example.desafio_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.desafio_backend.dto.UserDTO;
import com.example.desafio_backend.dto.WalletDTO;
import com.example.desafio_backend.entity.User;
import com.example.desafio_backend.entity.Wallet;
import com.example.desafio_backend.repository.UserRepository;
import com.example.desafio_backend.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User userSaved = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(userSaved);
        wallet.setBalance(10.0f);
        walletRepository.save(wallet);

        //establishes a relationship between the userSaved object and the wallet object, indicating that the wallet belongs to the userSaved
        userSaved.setWallet(wallet);

        return userSaved;
    }


    private UserDTO convertToDTO(User user) {
        //Null check is needed to ensure that the walletDTO is only created if the wallet field is not null.
        WalletDTO walletDTO = null;
        if (user.getWallet()!= null) {
            walletDTO = new WalletDTO(user.getWallet().getId(), user.getWallet().getBalance());
        }
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getCpf(),
            user.getEmail(),
            user.getType(),
            walletDTO
        );
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
