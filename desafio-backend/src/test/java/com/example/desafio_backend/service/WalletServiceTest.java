package com.example.desafio_backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.example.desafio_backend.dto.AuthorizationResponseDTO;
import com.example.desafio_backend.entity.User;
import com.example.desafio_backend.entity.Wallet;
import com.example.desafio_backend.error.UserNotFoundException;
import com.example.desafio_backend.repository.UserRepository;
import com.example.desafio_backend.repository.WalletRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void whenTransferFunds_thenWalletBalancesAreUpdated() {
        // Arrange
        Long payerId = 1L;
        Long payeeId = 2L;
        Float value = 10.0f;

        User payer = new User();
        payer.setId(payerId);
        payer.setType(User.UserType.Comum);
        Wallet payerWallet = new Wallet();
        payerWallet.setBalance(100.0f);
        payer.setWallet(payerWallet);

        User payee = new User();
        payee.setId(payeeId);
        Wallet payeeWallet = new Wallet();
        payeeWallet.setBalance(50.0f);
        payee.setWallet(payeeWallet);

        when(userRepository.findById(payerId)).thenReturn(java.util.Optional.of(payer));
        when(userRepository.findById(payeeId)).thenReturn(java.util.Optional.of(payee));

        AuthorizationResponseDTO response = new AuthorizationResponseDTO("success",
                new AuthorizationResponseDTO.Data(true));
        when(restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizationResponseDTO.class))
                .thenReturn(response);
        // Act
        walletService.transferFunds(value, payerId, payeeId);

        // Assert
        assertEquals(90.0f, payerWallet.getBalance());
        assertEquals(60.0f, payeeWallet.getBalance());
        verify(walletRepository).save(payerWallet);
        verify(walletRepository).save(payeeWallet);
    }

    @Test
    public void whenTransferFundsAndPayerIsLogista_thenIllegalArgumentExceptionIsThrown() {
        // Arrange
        Long payerId = 1L;
        Long payeeId = 2L;
        Float value = 10.0f;

        User payer = new User();
        payer.setId(payerId);
        payer.setType(User.UserType.Logista);
        Wallet payerWallet = new Wallet();
        payerWallet.setBalance(100.0f);
        payer.setWallet(payerWallet);

        when(userRepository.findById(payerId)).thenReturn(java.util.Optional.of(payer));

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> walletService.transferFunds(value, payerId, payeeId));
    }

    @Test
    public void whenTransferFundsAndExternalServiceFails_thenIllegalArgumentExceptionIsThrown() {
        // Arrange
        Long payerId = 1L;
        Long payeeId = 2L;
        Float value = 10.0f;

        User payer = new User();
        payer.setId(payerId);
        payer.setType(User.UserType.Comum);
        Wallet payerWallet = new Wallet();
        payerWallet.setBalance(100.0f);
        payer.setWallet(payerWallet);

        User payee = new User();
        payee.setId(payeeId);
        Wallet payeeWallet = new Wallet();
        payeeWallet.setBalance(50.0f);
        payee.setWallet(payeeWallet);

        when(userRepository.findById(payerId)).thenReturn(java.util.Optional.of(payer));
        when(userRepository.findById(payeeId)).thenReturn(java.util.Optional.of(payee));

        AuthorizationResponseDTO response = new AuthorizationResponseDTO("fail",
                new AuthorizationResponseDTO.Data(false));
        when(restTemplate.getForObject("https://util.devi.tools/api/v2/authorize", AuthorizationResponseDTO.class))
                .thenReturn(response);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> walletService.transferFunds(value, payerId, payeeId));
    }

}