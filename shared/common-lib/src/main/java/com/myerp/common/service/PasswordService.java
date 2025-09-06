package com.myerp.common.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço para criptografia de senhas usando BCrypt
 */
@Service
public class PasswordService {
    
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    /**
     * Criptografa uma senha em texto plano
     * @param rawPassword senha em texto plano
     * @return senha criptografada
     */
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    
    /**
     * Verifica se uma senha em texto plano corresponde à senha criptografada
     * @param rawPassword senha em texto plano
     * @param encodedPassword senha criptografada
     * @return true se as senhas correspondem
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}