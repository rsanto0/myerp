package com.myerp.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.myerp.auth.dto.LoginRequest;
import com.myerp.common.model.Usuario;
import com.myerp.common.enums.Role;
import com.myerp.common.service.PasswordService;
import com.myerp.auth.repository.UsuarioRepository;
import com.myerp.auth.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtService jwtService;
    
    @MockBean
    private PasswordService passwordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ValidCredentials_ReturnsAuthResponse() throws Exception {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setLogin("admin");
        user.setSenha("admin123");
        user.setRole(Role.ADMIN);

        LoginRequest request = new LoginRequest();
        request.setLogin("admin");
        request.setSenha("admin123");

        when(usuarioRepository.findByLogin("admin")).thenReturn(user);
        when(passwordService.matches("admin123", "admin123")).thenReturn(true);
        when(jwtService.generateToken("admin", "ADMIN", 1L)).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("admin");
        request.setSenha("wrong-password");

        Usuario user = new Usuario();
        user.setSenha("admin123");

        when(usuarioRepository.findByLogin("admin")).thenReturn(user);
        when(passwordService.matches("wrong-password", "admin123")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_UserNotFound_ReturnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setLogin("nonexistent");
        request.setSenha("password");

        when(usuarioRepository.findByLogin("nonexistent")).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


}