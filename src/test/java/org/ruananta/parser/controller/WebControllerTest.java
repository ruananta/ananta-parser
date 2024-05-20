package org.ruananta.parser.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ruananta.parser.config.Role;
import org.ruananta.parser.config.UserService;
import org.ruananta.parser.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WebControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private WebController webController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(webController).build();
    }

    @Test
    void testIndex() {
        WebController webController = new WebController();
        UserDetails mockUser = mock(UserDetails.class);
        Model mockModel = mock(Model.class);
        when(mockUser.getUsername()).thenReturn("testUser");
        String result = webController.index(mockUser, mockModel);
        verify(mockModel).addAttribute("username", "testUser");
    }

    @Test
    void testLogin() {
        WebController webController = new WebController();
        String result = webController.login();
        assertEquals("login", result);
    }

    @Test
    public void testRegisterUser() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/register")
                        .param("username", "testUser")
                        .param("email", "test@example.com")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/registration-successful"));

        verify(userService, times(1)).save(any());
    }

    @Test
    public void testUsernameAlreadyTaken() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("testUser");
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("password");

        when(userService.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/register")
                        .param("username", "testUser")
                        .param("email", "newtest@example.com")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(request().sessionAttribute("error", "Данное имя пользователя занято"));

        verify(userService, times(1)).findByUsername("testUser");
        verify(userService, never()).save(any(User.class));
    }
}
