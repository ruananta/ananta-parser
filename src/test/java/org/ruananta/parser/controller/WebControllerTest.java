package org.ruananta.parser.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ruananta.parser.config.InternationalizationConfig;
import org.ruananta.parser.config.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {InternationalizationConfig.class})
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

}
