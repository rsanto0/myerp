package com.myerp.company.service;

import com.myerp.company.entity.Empresa;
import com.myerp.company.enums.TipoPlano;
import com.myerp.company.repository.EmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpresaService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmpresaService.class);
    private final EmpresaRepository empresaRepository;
    
    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }
    
    public Empresa criarEmpresa(Empresa empresa) {
        logger.info("[EMPRESA] Criando empresa: {}", empresa.getRazaoSocial());
        
        // Validar CNPJ único
        if (empresa.getCnpj() != null) {
            Optional<Empresa> existente = empresaRepository.findByCnpj(empresa.getCnpj());
            if (existente.isPresent()) {
                throw new RuntimeException("CNPJ já cadastrado: " + empresa.getCnpj());
            }
        }
        
        // Definir grupo de manutenção baseado no plano
        if (empresa.getGrupoManutenção() == null) {
            empresa.setGrupoManutenção(empresa.getTipoPlano().getGrupoManutenção());
        }
        
        // Configurações padrão
        if (empresa.getTimezone() == null) {
            empresa.setTimezone("America/Sao_Paulo");
        }
        if (empresa.getMoeda() == null) {
            empresa.setMoeda("BRL");
        }
        
        Empresa salva = empresaRepository.save(empresa);
        logger.info("[EMPRESA] ✅ Empresa criada - ID: {}, CNPJ: {}", salva.getId(), salva.getCnpj());
        
        return salva;
    }
    
    public Optional<Empresa> buscarPorId(Long id) {
        return empresaRepository.findById(id);
    }
    
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }
    
    public List<Empresa> listarAtivas() {
        return empresaRepository.findByAtivaTrue();
    }
    
    public List<Empresa> listarPorPlano(TipoPlano plano) {
        return empresaRepository.findByTipoPlano(plano);
    }
    
    public List<Empresa> listarPorGrupoManutenção(String grupo) {
        return empresaRepository.findByGrupoManutenção(grupo);
    }
    
    public Empresa atualizarEmpresa(Long id, Empresa dadosAtualizacao) {
        logger.info("[EMPRESA] Atualizando empresa ID: {}", id);
        
        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
        
        // Atualizar campos permitidos
        if (dadosAtualizacao.getRazaoSocial() != null) {
            empresa.setRazaoSocial(dadosAtualizacao.getRazaoSocial());
        }
        if (dadosAtualizacao.getNomeFantasia() != null) {
            empresa.setNomeFantasia(dadosAtualizacao.getNomeFantasia());
        }
        if (dadosAtualizacao.getEndereco() != null) {
            empresa.setEndereco(dadosAtualizacao.getEndereco());
        }
        if (dadosAtualizacao.getTelefone() != null) {
            empresa.setTelefone(dadosAtualizacao.getTelefone());
        }
        if (dadosAtualizacao.getEmail() != null) {
            empresa.setEmail(dadosAtualizacao.getEmail());
        }
        
        empresa.setDataAtualizacao(LocalDateTime.now());
        
        Empresa atualizada = empresaRepository.save(empresa);
        logger.info("[EMPRESA] ✅ Empresa atualizada: {}", atualizada.getRazaoSocial());
        
        return atualizada;
    }
    
    public void ativarEmpresa(Long id) {
        logger.info("[EMPRESA] Ativando empresa ID: {}", id);
        
        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
        
        empresa.setAtiva(true);
        empresaRepository.save(empresa);
        
        logger.info("[EMPRESA] ✅ Empresa ativada: {}", empresa.getRazaoSocial());
    }
    
    public void desativarEmpresa(Long id) {
        logger.info("[EMPRESA] Desativando empresa ID: {}", id);
        
        Empresa empresa = empresaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + id));
        
        empresa.setAtiva(false);
        empresaRepository.save(empresa);
        
        logger.info("[EMPRESA] ⚠️ Empresa desativada: {}", empresa.getRazaoSocial());
    }
    
    // Feature Flags
    public void habilitarFeature(Long empresaId, String feature) {
        logger.info("[FEATURE_FLAG] Habilitando feature '{}' para empresa ID: {}", feature, empresaId);
        
        Empresa empresa = empresaRepository.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + empresaId));
        
        empresa.habilitarFeature(feature);
        empresaRepository.save(empresa);
        
        logger.info("[FEATURE_FLAG] ✅ Feature '{}' habilitada", feature);
    }
    
    public void desabilitarFeature(Long empresaId, String feature) {
        logger.info("[FEATURE_FLAG] Desabilitando feature '{}' para empresa ID: {}", feature, empresaId);
        
        Empresa empresa = empresaRepository.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + empresaId));
        
        empresa.desabilitarFeature(feature);
        empresaRepository.save(empresa);
        
        logger.info("[FEATURE_FLAG] ⚠️ Feature '{}' desabilitada", feature);
    }
    
    public boolean isFeatureAtiva(Long empresaId, String feature) {
        Optional<Empresa> empresa = empresaRepository.findById(empresaId);
        return empresa.map(e -> e.isFeatureAtiva(feature)).orElse(false);
    }
    
    // Configurações
    public void adicionarConfiguracao(Long empresaId, String chave, String valor) {
        logger.info("[CONFIG] Adicionando configuração '{}' para empresa ID: {}", chave, empresaId);
        
        Empresa empresa = empresaRepository.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + empresaId));
        
        empresa.adicionarConfiguracao(chave, valor);
        empresaRepository.save(empresa);
        
        logger.info("[CONFIG] ✅ Configuração '{}' adicionada", chave);
    }
    
    public String getConfiguracao(Long empresaId, String chave) {
        Optional<Empresa> empresa = empresaRepository.findById(empresaId);
        return empresa.map(e -> e.getConfiguracao(chave)).orElse(null);
    }
    
    public long contarEmpresasAtivas() {
        return empresaRepository.countEmpresasAtivas();
    }
}