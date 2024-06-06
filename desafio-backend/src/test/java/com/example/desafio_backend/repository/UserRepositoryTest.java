package com.example.desafio_backend.repository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.desafio_backend.entity.User;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        // Create a test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("testuser");
        user.setCpf("11111111111");
        user.setPassword("password");
        user.setType(User.UserType.Comum);

        entityManager.persist(user);
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail("test@example.com");

        // Assert that the user is found
        assertTrue(userOptional.isPresent());
        User foundUser = userOptional.get();
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("testuser", foundUser.getName());
    }

    @Test
    public void whenFindByNonExistingEmail_thenReturnNull() {
        // Find user by non-existing email
        Optional<User> userOptional = userRepository.findByEmail("non-existent@example.com");

        // Assert that the user is not found
        assertFalse(userOptional.isPresent());
    }

}
