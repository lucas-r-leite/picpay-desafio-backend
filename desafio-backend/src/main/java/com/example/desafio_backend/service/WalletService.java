package com.example.desafio_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.desafio_backend.entity.User;
import com.example.desafio_backend.entity.Wallet;
import com.example.desafio_backend.error.InsufficientFundsException;
import com.example.desafio_backend.error.UserNotFoundException;
import com.example.desafio_backend.repository.UserRepository;
import com.example.desafio_backend.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletService {

     @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void transferFunds(Float value, Long payerId, Long payeeId) {
        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));
        User payee = userRepository.findById(payeeId)
                .orElseThrow(() -> new UserNotFoundException("Payee not found"));

        Wallet payerWallet = payer.getWallet();
        Wallet payeeWallet = payee.getWallet();

        // Check if the payer is a "Logista"
        if (payer.getType() == User.UserType.Logista) {
            throw new IllegalArgumentException("Logista users cannot send funds");
        }

        if (payerWallet.getBalance() < value) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        payerWallet.setBalance(payerWallet.getBalance() - value);
        payeeWallet.setBalance(payeeWallet.getBalance() + value);

        walletRepository.save(payerWallet);
        walletRepository.save(payeeWallet);
    }
}
