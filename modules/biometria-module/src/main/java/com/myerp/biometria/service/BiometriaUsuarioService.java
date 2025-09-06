package com.myerp.biometria.service;

import com.myerp.biometria.entity.BiometriaUsuario;
import com.myerp.biometria.entity.TipoBiometria;
import com.myerp.biometria.repository.BiometriaUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BiometriaUsuarioService {
    
    private static final Logger logger = LoggerFactory.getLogger(BiometriaUsuarioService.class);
    private final BiometriaUsuarioRepository repository;
    
    public BiometriaUsuarioService(BiometriaUsuarioRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Cadastra nova biometria para usuário
     */
    public BiometriaUsuario cadastrarBiometria(Long usuarioId, String nomeUsuario, 
                                             TipoBiometria tipo, String dadosBase64) {
        logger.info("[BIOMETRIA_CADASTRO] Iniciando cadastro - Usuário: {}, Tipo: {}", 
                   nomeUsuario, tipo);
        
        try {
            // Verificar se já existe biometria para o usuário
            if (repository.existsByUsuarioIdAndAtivoTrue(usuarioId)) {
                logger.warn("[BIOMETRIA_CADASTRO] Usuário {} já possui biometria cadastrada", usuarioId);
                throw new IllegalStateException("Usuário já possui biometria cadastrada");
            }
            
            // Criar nova biometria
            BiometriaUsuario biometria = new BiometriaUsuario(usuarioId, nomeUsuario, tipo);
            biometria.setDadosBiometricos(dadosBase64);
            biometria.setHashBiometria(gerarHash(dadosBase64));
            biometria.setQualidadeCaptura(calcularQualidade(dadosBase64));
            
            BiometriaUsuario salva = repository.save(biometria);
            
            logger.info("[BIOMETRIA_CADASTRO] ✅ Biometria cadastrada - ID: {}, Usuário: {}, Qualidade: {}%", 
                       salva.getId(), nomeUsuario, (salva.getQualidadeCaptura() * 100));
            
            return salva;
            
        } catch (Exception e) {
            logger.error("[BIOMETRIA_CADASTRO] ❌ Erro ao cadastrar biometria para {}: {}", 
                        nomeUsuario, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Busca biometria do usuário
     */
    public Optional<BiometriaUsuario> buscarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }
    
    /**
     * Verifica se usuário tem biometria
     */
    public boolean temBiometria(Long usuarioId) {
        return repository.existsByUsuarioIdAndAtivoTrue(usuarioId);
    }
    
    /**
     * Remove biometria do usuário (soft delete)
     */
    public void removerBiometria(Long usuarioId) {
        logger.info("[BIOMETRIA_REMOCAO] Removendo biometria do usuário: {}", usuarioId);
        
        Optional<BiometriaUsuario> biometria = repository.findByUsuarioIdAndAtivoTrue(usuarioId);
        if (biometria.isPresent()) {
            BiometriaUsuario bio = biometria.get();
            bio.setAtivo(false);
            repository.save(bio);
            
            logger.info("[BIOMETRIA_REMOCAO] ✅ Biometria removida - Usuário: {}", usuarioId);
        } else {
            logger.warn("[BIOMETRIA_REMOCAO] Usuário {} não possui biometria ativa", usuarioId);
        }
    }
    
    /**
     * Lista todas as biometrias por tipo
     */
    public List<BiometriaUsuario> listarPorTipo(TipoBiometria tipo) {
        return repository.findByTipoBiometriaAndAtivoTrue(tipo);
    }
    
    /**
     * Estatísticas de biometrias
     */
    public BiometriaStats obterEstatisticas() {
        long totalFacial = repository.countByTipo(TipoBiometria.FACIAL);
        long totalDigital = repository.countByTipo(TipoBiometria.DIGITAL);
        long totalIris = repository.countByTipo(TipoBiometria.IRIS);
        long totalVoz = repository.countByTipo(TipoBiometria.VOZ);
        
        return new BiometriaStats(totalFacial, totalDigital, totalIris, totalVoz);
    }
    
    /**
     * Gera hash SHA-256 dos dados biométricos
     */
    private String gerarHash(String dados) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dados.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            logger.error("[BIOMETRIA_HASH] Erro ao gerar hash: {}", e.getMessage());
            return "";
        }
    }
    
    /**
     * Simula cálculo de qualidade da captura
     */
    private Double calcularQualidade(String dadosBase64) {
        // Simulação: qualidade baseada no tamanho dos dados
        int tamanho = dadosBase64.length();
        if (tamanho > 50000) return 0.95; // Excelente
        if (tamanho > 30000) return 0.85; // Boa
        if (tamanho > 15000) return 0.70; // Regular
        return 0.50; // Baixa
    }
    
    /**
     * Classe para estatísticas
     */
    public static class BiometriaStats {
        private final long facial;
        private final long digital;
        private final long iris;
        private final long voz;
        private final long total;
        
        public BiometriaStats(long facial, long digital, long iris, long voz) {
            this.facial = facial;
            this.digital = digital;
            this.iris = iris;
            this.voz = voz;
            this.total = facial + digital + iris + voz;
        }
        
        // Getters
        public long getFacial() { return facial; }
        public long getDigital() { return digital; }
        public long getIris() { return iris; }
        public long getVoz() { return voz; }
        public long getTotal() { return total; }
    }
}