package com.myerp.financial.service;

import com.myerp.financial.client.CompanyClient;
import com.myerp.financial.entity.TransacaoPix;
import com.myerp.financial.enums.StatusPix;
import com.myerp.financial.repository.TransacaoPixRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PixService {
    
    private static final Logger logger = LoggerFactory.getLogger(PixService.class);
    
    @Autowired
    private TransacaoPixRepository transacaoPixRepository;
    
    @Autowired
    private CompanyClient companyClient;
    
    public TransacaoPix criarTransacaoPix(TransacaoPix transacao) {
        logger.info("[CRIAR_PIX] Iniciando criação de transação PIX - Empresa: {}, Valor: {}", 
                   transacao.getEmpresaId(), transacao.getValor());
        
        // Validar conta bancária
        validarContaBancaria(transacao.getEmpresaId(), transacao.getContaBancariaId());
        
        // Gerar IDs únicos
        gerarIdentificadoresPix(transacao);
        
        // Gerar QR Code e Payload
        gerarQrCodePix(transacao);
        
        TransacaoPix transacaoSalva = transacaoPixRepository.save(transacao);
        
        logger.info("[CRIAR_PIX] Transação PIX criada com sucesso - ID: {}, TxID: {}", 
                   transacaoSalva.getId(), transacaoSalva.getTxid());
        
        return transacaoSalva;
    }
    
    public List<TransacaoPix> listarTransacoesPorEmpresa(Long empresaId) {
        logger.info("[LISTAR_PIX] Listando transações PIX da empresa: {}", empresaId);
        return transacaoPixRepository.findByEmpresaId(empresaId);
    }
    
    public List<TransacaoPix> listarTransacoesPorStatus(Long empresaId, StatusPix status) {
        logger.info("[LISTAR_PIX_STATUS] Listando transações PIX - Empresa: {}, Status: {}", empresaId, status);
        return transacaoPixRepository.findByEmpresaIdAndStatus(empresaId, status);
    }
    
    public Optional<TransacaoPix> buscarPorTxid(String txid) {
        logger.info("[BUSCAR_PIX] Buscando transação PIX por TxID: {}", txid);
        return transacaoPixRepository.findByTxid(txid);
    }
    
    public Optional<TransacaoPix> buscarPorEndToEndId(String endToEndId) {
        logger.info("[BUSCAR_PIX_E2E] Buscando transação PIX por EndToEndId: {}", endToEndId);
        return transacaoPixRepository.findByEndToEndId(endToEndId);
    }
    
    public TransacaoPix processarPagamentoPix(Long transacaoId) {
        logger.info("[PROCESSAR_PIX] Processando pagamento PIX - ID: {}", transacaoId);
        
        TransacaoPix transacao = transacaoPixRepository.findById(transacaoId)
                .orElseThrow(() -> new RuntimeException("Transação PIX não encontrada"));
        
        transacao.setStatus(StatusPix.APROVADO);
        transacao.setDataProcessamento(LocalDateTime.now());
        
        TransacaoPix transacaoAtualizada = transacaoPixRepository.save(transacao);
        
        logger.info("[PROCESSAR_PIX] Pagamento PIX processado com sucesso - ID: {}", transacaoId);
        
        return transacaoAtualizada;
    }
    
    public TransacaoPix cancelarTransacaoPix(Long transacaoId, String motivo) {
        logger.info("[CANCELAR_PIX] Cancelando transação PIX - ID: {}, Motivo: {}", transacaoId, motivo);
        
        TransacaoPix transacao = transacaoPixRepository.findById(transacaoId)
                .orElseThrow(() -> new RuntimeException("Transação PIX não encontrada"));
        
        transacao.setStatus(StatusPix.CANCELADO);
        transacao.setDescricao(transacao.getDescricao() + " - CANCELADO: " + motivo);
        
        TransacaoPix transacaoAtualizada = transacaoPixRepository.save(transacao);
        
        logger.info("[CANCELAR_PIX] Transação PIX cancelada com sucesso - ID: {}", transacaoId);
        
        return transacaoAtualizada;
    }
    
    public List<TransacaoPix> listarTransacoesPorPeriodo(Long empresaId, LocalDateTime inicio, LocalDateTime fim) {
        logger.info("[PIX_PERIODO] Listando transações PIX - Empresa: {}, Período: {} a {}", 
                   empresaId, inicio, fim);
        return transacaoPixRepository.findTransacoesPorPeriodo(empresaId, inicio, fim);
    }
    
    public List<TransacaoPix> listarTransacoesPorChave(String chavePix) {
        logger.info("[PIX_CHAVE] Listando transações PIX por chave: {}", chavePix);
        return transacaoPixRepository.findByChavePix(chavePix);
    }
    
    public Map<String, Object> obterEstatisticas(Long empresaId) {
        logger.info("[ESTATISTICAS_PIX] Calculando estatísticas PIX da empresa: {}", empresaId);
        
        Long pendentes = transacaoPixRepository.countByEmpresaIdAndStatus(empresaId, StatusPix.PENDENTE);
        Long aprovadas = transacaoPixRepository.countByEmpresaIdAndStatus(empresaId, StatusPix.APROVADO);
        Long rejeitadas = transacaoPixRepository.countByEmpresaIdAndStatus(empresaId, StatusPix.REJEITADO);
        Long canceladas = transacaoPixRepository.countByEmpresaIdAndStatus(empresaId, StatusPix.CANCELADO);
        Long devolvidas = transacaoPixRepository.countByEmpresaIdAndStatus(empresaId, StatusPix.DEVOLVIDO);
        
        return Map.of(
            "pendentes", pendentes,
            "aprovadas", aprovadas,
            "rejeitadas", rejeitadas,
            "canceladas", canceladas,
            "devolvidas", devolvidas,
            "total", pendentes + aprovadas + rejeitadas + canceladas + devolvidas
        );
    }
    
    private void validarContaBancaria(Long empresaId, Long contaBancariaId) {
        try {
            List<Map<String, Object>> contas = companyClient.getContasComPix(empresaId);
            boolean contaValida = contas.stream()
                    .anyMatch(conta -> conta.get("id").equals(contaBancariaId));
            
            if (!contaValida) {
                throw new RuntimeException("Conta bancária não habilitada para PIX");
            }
        } catch (Exception e) {
            logger.warn("[VALIDAR_CONTA_PIX] Erro ao validar conta PIX: {}", e.getMessage());
            // Em caso de erro na integração, permite continuar
        }
    }
    
    private void gerarIdentificadoresPix(TransacaoPix transacao) {
        // Gerar TxID único
        String txid = UUID.randomUUID().toString().replace("-", "").substring(0, 25);
        transacao.setTxid(txid);
        
        // Gerar EndToEndId único
        String endToEndId = "E" + String.format("%08d", System.currentTimeMillis() % 100000000) + 
                           String.format("%04d", (int)(Math.random() * 10000)) + 
                           String.format("%014d", System.currentTimeMillis() % 100000000000000L);
        transacao.setEndToEndId(endToEndId);
    }
    
    private void gerarQrCodePix(TransacaoPix transacao) {
        // Simulação de geração de QR Code PIX (em produção usar biblioteca específica)
        String payload = String.format(
            "00020126580014br.gov.bcb.pix0136%s5204000053039865802BR5925%s6009SAO PAULO62070503***6304",
            transacao.getChavePix(),
            transacao.getRecebedorNome() != null ? transacao.getRecebedorNome() : "EMPRESA"
        );
        
        transacao.setPayloadPix(payload);
        transacao.setQrCode("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==");
    }
}