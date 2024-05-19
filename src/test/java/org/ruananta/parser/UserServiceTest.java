package org.ruananta.parser;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ruananta.parser.config.UserService;
import org.ruananta.parser.entity.User;
import org.ruananta.parser.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    public User getTestUser(){
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@test.com");
        user.setPassword("password123");
        user.setRole("USER");

        user.setEndingDate(new Date(System.currentTimeMillis()));
        return user;
    }
    @Test
    public void testFindByEmail() {
        User user = getTestUser();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User found = userService.findByEmail(user.getEmail()).get();
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        assertNotNull(found);
        assertEquals(user.getEmail(), found.getEmail());
        assertEquals(user.getUsername(), found.getUsername());
        assertEquals(user.getPassword(), found.getPassword());
        assertEquals(user.getRole(), found.getRole());
        assertEquals(user.getEndingDate(), found.getEndingDate());
    }
    @Test
    public void testFindByUsername() {
        User user = getTestUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        User found = userService.findByUsername(user.getUsername()).get();
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
        assertEquals(user.getEmail(), found.getEmail());
        assertEquals(user.getPassword(), found.getPassword());
        assertEquals(user.getRole(), found.getRole());
        assertEquals(user.getEndingDate(), found.getEndingDate());
    }
}
