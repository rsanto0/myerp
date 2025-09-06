package com.myerp.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "testsecretkey123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
    }

    @Test
    void generateToken_ValidUser_ReturnsJWT() {
        String token = jwtService.generateToken("admin", "ADMIN", 1L);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.contains("."));
    }

    @Test
    void validateToken_ValidJWT_ReturnsClaims() {
        String token = jwtService.generateToken("admin", "ADMIN", 1L);
        
        Claims claims = jwtService.validateToken(token);
        
        assertEquals("admin", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
        assertEquals(1, claims.get("userId"));
    }

    @Test
    void validateToken_InvalidJWT_ThrowsException() {
        String invalidToken = "invalid.jwt.token";
        
        assertThrows(MalformedJwtException.class, () -> {
            jwtService.validateToken(invalidToken);
        });
    }

    @Test
    void validateToken_ExpiredJWT_ThrowsException() {
        // Criar service com expiração muito baixa
        JwtService expiredService = new JwtService();
        ReflectionTestUtils.setField(expiredService, "secret", "testsecretkey123456789012345678901234567890");
        ReflectionTestUtils.setField(expiredService, "expiration", -1L);
        
        String expiredToken = expiredService.generateToken("admin", "ADMIN", 1L);
        
        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.validateToken(expiredToken);
        });
    }
}