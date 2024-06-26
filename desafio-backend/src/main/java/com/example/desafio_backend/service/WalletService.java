package com.example.desafio_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.desafio_backend.dto.AuthorizationResponseDTO;
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

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void transferFunds(Float value, Long payerId, Long payeeId) {
        if (value == null || payerId == null || payeeId == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be greater than zero");
        }
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

        String url = "https://util.devi.tools/api/v2/authorize";
        AuthorizationResponseDTO response = restTemplate.getForObject(url, AuthorizationResponseDTO.class);

        if (response == null || "fail".equals(response.status()) || !response.data().authorization()) {
            throw new IllegalArgumentException("Transfer not authorized by external service");
        }

        payerWallet.setBalance(payerWallet.getBalance() - value);
        payeeWallet.setBalance(payeeWallet.getBalance() + value);

        walletRepository.save(payerWallet);
        walletRepository.save(payeeWallet);

        // Send notification after successful transfer
    Map<String, Object> notifyRequest = new HashMap<>();
    notifyRequest.put("payerId", payerId);
    notifyRequest.put("payeeId", payeeId);
    notifyRequest.put("value", value);
    notifyRequest.put("message", "Transfer works");

    String notifyUrl = "https://util.devi.tools/api/v1/notify";
    restTemplate.postForObject(notifyUrl, notifyRequest, Void.class);
    }
}
