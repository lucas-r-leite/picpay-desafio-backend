package com.example.desafio_backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.example.desafio_backend.dto.UserDTO;
import com.example.desafio_backend.entity.User;
import com.example.desafio_backend.entity.Wallet;
import com.example.desafio_backend.repository.UserRepository;
import com.example.desafio_backend.repository.WalletRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenSaveUser_thenUserIsSaved() {
        // Given
        User user = new User();
        user.setName("John Doe");
        user.setCpf("1234567890");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setType(User.UserType.Comum);

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(user)).thenReturn(user);

        // When
        User savedUser = userService.saveUser(user);

        // Then
        assertNotNull(savedUser);
        assertEquals("encoded_password", savedUser.getPassword());
        verify(walletRepository).save(any(Wallet.class));
    }

   @Test
    public void whenSaveUser_thenWalletIsCreated() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("testuser");
        user.setCpf("11111111111");
        user.setPassword("password");
        user.setType(User.UserType.Comum);

        Wallet wallet = new Wallet();
        wallet.setBalance(10.0f);

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(user)).thenReturn(user);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        // When
        User savedUser = userService.saveUser(user);

        // Then
        assertNotNull(savedUser);
        assertNotNull(savedUser.getWallet());
        assertEquals(10.0f, savedUser.getWallet().getBalance());
    }


    @Test
    public void whenGetUsers_thenUsersAreReturned() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> userDTOS = userService.getUsers();

        // Assert
        assertEquals(2, userDTOS.size());
        assertEquals(users.get(0).getId(), userDTOS.get(0).id());
        assertEquals(users.get(1).getId(), userDTOS.get(1).id()); 
    }
}