package com.myerp.rh.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Cliente Feign para integração com o Biometria Module.
 * 
 * <p><b>PROCESSO DE NEGÓCIO:</b> Esta classe faz parte do processo de <b>Integração RH-Biometria</b>
 * e trabalha em conjunto com o <b>Biometria Module (8083)</b> para cadastrar dados biométricos
 * dos funcionários durante o processo de criação no RH Module.
 * 
 * <p><b>INTEGRAÇÕES:</b>
 * <ul>
 *   <li><b>Biometria Module (8083)</b>: Consome APIs de cadastro biométrico</li>
 *   <li><b>RH Module (8082)</b>: Usado pelo {@code AdminController} para cadastro completo</li>
 *   <li><b>Service Discovery (8761)</b>: Descoberta automática do serviço biometria-service</li>
 *   <li><b>API Gateway (8080)</b>: Roteamento transparente entre módulos</li>
 * </ul>
 * 
 * <p><b>FUNCIONALIDADES:</b>
 * <ul>
 *   <li><b>Cadastro biométrico</b>: Registra dados biométricos durante criação de funcionário</li>
 *   <li><b>Verificação</b>: Consulta se usuário possui biometria cadastrada</li>
 *   <li><b>Remoção</b>: Remove dados biométricos quando funcionário é excluído</li>
 *   <li><b>Fallback</b>: Tratamento de erro via {@code BiometriaServiceClientFallback}</li>
 * </ul>
 * 
 * <p><b>FLUXO DE USO:</b>
 * <ol>
 *   <li>Admin cria funcionário via {@code AdminController}</li>
 *   <li>RH Module chama {@code cadastrarBiometria()} automaticamente</li>
 *   <li>Biometria Module processa e armazena dados biométricos</li>
 *   <li>Sistema fica pronto para controle de ponto biométrico</li>
 * </ol>
 * 
 * <p><b>TRATAMENTO DE ERRO:</b>
 * Em caso de falha na comunicação, o fallback permite que o cadastro de funcionário
 * continue normalmente, apenas sem a integração biométrica.
 * 
 * @author MyERP Team
 * @version 1.0
 * @since 1.0
 * @see BiometriaServiceClientFallback
 * @see com.myerp.rh.controller.AdminController
 * @see com.myerp.biometria.controller.BiometriaController
 */
@FeignClient(name = "biometria-service", url = "http://localhost:8083", fallback = BiometriaServiceClientFallback.class)
public interface BiometriaServiceClient {
    
    /**
     * Cadastra biometria para usuário
     */
    @PostMapping("/api/biometria/usuarios/cadastrar")
    Map<String, Object> cadastrarBiometria(@RequestBody CadastrarBiometriaRequest request);
    
    /**
     * Verifica se usuário tem biometria
     */
    @GetMapping("/api/biometria/usuarios/verificar/{usuarioId}")
    Map<String, Object> verificarBiometria(@PathVariable Long usuarioId);
    
    /**
     * Remove biometria do usuário
     */
    @DeleteMapping("/api/biometria/usuarios/remover/{usuarioId}")
    Map<String, Object> removerBiometria(@PathVariable Long usuarioId);
    
    /**
     * DTO para cadastro de biometria
     */
    class CadastrarBiometriaRequest {
        private Long usuarioId;
        private String nomeUsuario;
        private String tipo; // FACIAL, DIGITAL, IRIS, VOZ
        private String dadosBiometricos; // Base64
        
        public CadastrarBiometriaRequest() {}
        
        public CadastrarBiometriaRequest(Long usuarioId, String nomeUsuario, String tipo, String dadosBiometricos) {
            this.usuarioId = usuarioId;
            this.nomeUsuario = nomeUsuario;
            this.tipo = tipo;
            this.dadosBiometricos = dadosBiometricos;
        }
        
        // Getters e Setters
        public Long getUsuarioId() { return usuarioId; }
        public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
        
        public String getNomeUsuario() { return nomeUsuario; }
        public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getDadosBiometricos() { return dadosBiometricos; }
        public void setDadosBiometricos(String dadosBiometricos) { this.dadosBiometricos = dadosBiometricos; }
    }
}