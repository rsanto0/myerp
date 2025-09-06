package com.myerp.company.service;

import com.myerp.company.entity.ContaBancaria;
import com.myerp.company.enums.BancoBrasil;
import com.myerp.company.enums.TipoConta;
import com.myerp.company.repository.ContaBancariaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContaBancariaService {
    
    private static final Logger logger = LoggerFactory.getLogger(ContaBancariaService.class);
    private final ContaBancariaRepository contaBancariaRepository;
    
    public ContaBancariaService(ContaBancariaRepository contaBancariaRepository) {
        this.contaBancariaRepository = contaBancariaRepository;
    }
    
    public ContaBancaria criarContaBancaria(ContaBancaria conta) {
        logger.info("[CONTA_BANCARIA] Criando conta: {} - Banco: {}", 
                   conta.getNomeConta(), conta.getBanco().getCodigoNome());
        
        // Validar se já existe conta com mesma agência/conta
        Optional<ContaBancaria> existente = contaBancariaRepository
            .findByEmpresaIdAndAgenciaAndConta(conta.getEmpresaId(), conta.getAgencia(), conta.getConta());
        
        if (existente.isPresent()) {
            throw new RuntimeException("Já existe conta cadastrada: " + conta.getAgencia() + "/" + conta.getConta());
        }
        
        // Se for marcada como principal, desmarcar outras
        if (conta.getContaPrincipal()) {
            desmarcarContaPrincipal(conta.getEmpresaId());
        }
        
        // Criptografar dados sensíveis (implementar depois)
        if (conta.getClientSecret() != null) {
            conta.setClientSecret(criptografarDado(conta.getClientSecret()));
        }
        
        ContaBancaria salva = contaBancariaRepository.save(conta);
        logger.info("[CONTA_BANCARIA] ✅ Conta criada - ID: {}, Banco: {}", 
                   salva.getId(), salva.getBanco().getCodigo());
        
        return salva;
    }
    
    public List<ContaBancaria> listarContasPorEmpresa(Long empresaId) {
        return contaBancariaRepository.findByEmpresaIdAndAtivaTrue(empresaId);
    }
    
    public List<ContaBancaria> listarContasPorBanco(Long empresaId, BancoBrasil banco) {
        return contaBancariaRepository.findByEmpresaIdAndBanco(empresaId, banco);
    }
    
    public List<ContaBancaria> listarContasComBoletos(Long empresaId) {
        return contaBancariaRepository.findByEmpresaIdAndAtivaBoletosTrue(empresaId);
    }
    
    public List<ContaBancaria> listarContasComPix(Long empresaId) {
        return contaBancariaRepository.findByEmpresaIdAndAtivaPixTrue(empresaId);
    }
    
    public Optional<ContaBancaria> buscarContaPrincipal(Long empresaId) {
        return contaBancariaRepository.findByEmpresaIdAndContaPrincipalTrue(empresaId);
    }
    
    public Optional<ContaBancaria> buscarPorId(Long id) {
        return contaBancariaRepository.findById(id);
    }
    
    public ContaBancaria atualizarConta(Long id, ContaBancaria dadosAtualizacao) {
        logger.info("[CONTA_BANCARIA] Atualizando conta ID: {}", id);
        
        ContaBancaria conta = contaBancariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + id));
        
        // Atualizar campos permitidos
        if (dadosAtualizacao.getNomeConta() != null) {
            conta.setNomeConta(dadosAtualizacao.getNomeConta());
        }
        if (dadosAtualizacao.getTitular() != null) {
            conta.setTitular(dadosAtualizacao.getTitular());
        }
        if (dadosAtualizacao.getClientId() != null) {
            conta.setClientId(dadosAtualizacao.getClientId());
        }
        if (dadosAtualizacao.getClientSecret() != null) {
            conta.setClientSecret(criptografarDado(dadosAtualizacao.getClientSecret()));
        }
        if (dadosAtualizacao.getAmbiente() != null) {
            conta.setAmbiente(dadosAtualizacao.getAmbiente());
        }
        if (dadosAtualizacao.getObservacoes() != null) {
            conta.setObservacoes(dadosAtualizacao.getObservacoes());
        }
        
        // Atualizar funcionalidades
        if (dadosAtualizacao.getAtivaBoletos() != null) {
            conta.setAtivaBoletos(dadosAtualizacao.getAtivaBoletos());
        }
        if (dadosAtualizacao.getAtivaPix() != null) {
            conta.setAtivaPix(dadosAtualizacao.getAtivaPix());
        }
        if (dadosAtualizacao.getAtivaTedDoc() != null) {
            conta.setAtivaTedDoc(dadosAtualizacao.getAtivaTedDoc());
        }
        if (dadosAtualizacao.getAtivaConciliacao() != null) {
            conta.setAtivaConciliacao(dadosAtualizacao.getAtivaConciliacao());
        }
        
        // Se for marcada como principal, desmarcar outras
        if (dadosAtualizacao.getContaPrincipal() != null && dadosAtualizacao.getContaPrincipal()) {
            desmarcarContaPrincipal(conta.getEmpresaId());
            conta.setContaPrincipal(true);
        }
        
        conta.setDataAtualizacao(LocalDateTime.now());
        
        ContaBancaria atualizada = contaBancariaRepository.save(conta);
        logger.info("[CONTA_BANCARIA] ✅ Conta atualizada: {}", atualizada.getNomeConta());
        
        return atualizada;
    }
    
    public void ativarConta(Long id) {
        logger.info("[CONTA_BANCARIA] Ativando conta ID: {}", id);
        
        ContaBancaria conta = contaBancariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + id));
        
        conta.setAtiva(true);
        contaBancariaRepository.save(conta);
        
        logger.info("[CONTA_BANCARIA] ✅ Conta ativada: {}", conta.getNomeConta());
    }
    
    public void desativarConta(Long id) {
        logger.info("[CONTA_BANCARIA] Desativando conta ID: {}", id);
        
        ContaBancaria conta = contaBancariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + id));
        
        // Não permitir desativar conta principal
        if (conta.getContaPrincipal()) {
            throw new RuntimeException("Não é possível desativar a conta principal");
        }
        
        conta.setAtiva(false);
        contaBancariaRepository.save(conta);
        
        logger.info("[CONTA_BANCARIA] ⚠️ Conta desativada: {}", conta.getNomeConta());
    }
    
    public void definirContaPrincipal(Long id) {
        logger.info("[CONTA_BANCARIA] Definindo conta principal ID: {}", id);
        
        ContaBancaria conta = contaBancariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + id));
        
        // Desmarcar outras contas principais
        desmarcarContaPrincipal(conta.getEmpresaId());
        
        // Marcar como principal
        conta.setContaPrincipal(true);
        conta.setAtiva(true); // Conta principal deve estar ativa
        contaBancariaRepository.save(conta);
        
        logger.info("[CONTA_BANCARIA] ✅ Conta principal definida: {}", conta.getNomeConta());
    }
    
    public void sincronizarSaldo(Long id) {
        logger.info("[CONTA_BANCARIA] Sincronizando saldo da conta ID: {}", id);
        
        ContaBancaria conta = contaBancariaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada: " + id));
        
        if (!conta.isIntegracaoAtiva()) {
            throw new RuntimeException("Conta não possui integração ativa");
        }
        
        // TODO: Implementar integração com API do banco
        conta.setUltimaSincronizacao(LocalDateTime.now());
        contaBancariaRepository.save(conta);
        
        logger.info("[CONTA_BANCARIA] ✅ Saldo sincronizado: {}", conta.getNomeConta());
    }
    
    public long contarContasAtivas(Long empresaId) {
        return contaBancariaRepository.countByEmpresaIdAndAtivaTrue(empresaId);
    }
    
    // Métodos auxiliares
    private void desmarcarContaPrincipal(Long empresaId) {
        List<ContaBancaria> contas = contaBancariaRepository.findByEmpresaId(empresaId);
        contas.forEach(conta -> {
            if (conta.getContaPrincipal()) {
                conta.setContaPrincipal(false);
                contaBancariaRepository.save(conta);
            }
        });
    }
    
    private String criptografarDado(String dado) {
        // TODO: Implementar criptografia real (AES-256)
        // Por enquanto, apenas um placeholder
        return "ENCRYPTED:" + dado;
    }
    
    private String descriptografarDado(String dadoCriptografado) {
        // TODO: Implementar descriptografia real
        if (dadoCriptografado.startsWith("ENCRYPTED:")) {
            return dadoCriptografado.substring(10);
        }
        return dadoCriptografado;
    }
}