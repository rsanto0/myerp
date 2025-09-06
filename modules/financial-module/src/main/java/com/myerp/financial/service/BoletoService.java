package com.myerp.financial.service;

import com.myerp.financial.client.CompanyClient;
import com.myerp.financial.entity.Boleto;
import com.myerp.financial.enums.StatusBoleto;
import com.myerp.financial.repository.BoletoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoletoService {
    
    private static final Logger logger = LoggerFactory.getLogger(BoletoService.class);
    
    @Autowired
    private BoletoRepository boletoRepository;
    
    @Autowired
    private CompanyClient companyClient;
    
    public Boleto criarBoleto(Boleto boleto) {
        logger.info("[CRIAR_BOLETO] Iniciando criação de boleto - Empresa: {}, Valor: {}", 
                   boleto.getEmpresaId(), boleto.getValor());
        
        // Validar conta bancária
        validarContaBancaria(boleto.getEmpresaId(), boleto.getContaBancariaId());
        
        // Gerar códigos do boleto
        gerarCodigosBoleto(boleto);
        
        Boleto boletoSalvo = boletoRepository.save(boleto);
        
        logger.info("[CRIAR_BOLETO] Boleto criado com sucesso - ID: {}, Linha Digitável: {}", 
                   boletoSalvo.getId(), boletoSalvo.getLinhaDigitavel());
        
        return boletoSalvo;
    }
    
    public List<Boleto> listarBoletosPorEmpresa(Long empresaId) {
        logger.info("[LISTAR_BOLETOS] Listando boletos da empresa: {}", empresaId);
        return boletoRepository.findByEmpresaId(empresaId);
    }
    
    public List<Boleto> listarBoletosPorStatus(Long empresaId, StatusBoleto status) {
        logger.info("[LISTAR_BOLETOS_STATUS] Listando boletos - Empresa: {}, Status: {}", empresaId, status);
        return boletoRepository.findByEmpresaIdAndStatus(empresaId, status);
    }
    
    public Optional<Boleto> buscarPorLinhaDigitavel(String linhaDigitavel) {
        logger.info("[BUSCAR_BOLETO] Buscando boleto por linha digitável: {}", linhaDigitavel);
        return boletoRepository.findByLinhaDigitavel(linhaDigitavel);
    }
    
    public Boleto processarPagamento(Long boletoId, BigDecimal valorPago) {
        logger.info("[PROCESSAR_PAGAMENTO] Processando pagamento - Boleto: {}, Valor: {}", boletoId, valorPago);
        
        Boleto boleto = boletoRepository.findById(boletoId)
                .orElseThrow(() -> new RuntimeException("Boleto não encontrado"));
        
        boleto.setValorPago(valorPago);
        boleto.setDataPagamento(LocalDate.now());
        boleto.setStatus(StatusBoleto.PAGO);
        
        Boleto boletoAtualizado = boletoRepository.save(boleto);
        
        logger.info("[PROCESSAR_PAGAMENTO] Pagamento processado com sucesso - Boleto: {}", boletoId);
        
        return boletoAtualizado;
    }
    
    public List<Boleto> listarBoletosVencidos(Long empresaId) {
        logger.info("[BOLETOS_VENCIDOS] Listando boletos vencidos da empresa: {}", empresaId);
        return boletoRepository.findBoletosVencidos(empresaId, LocalDate.now());
    }
    
    public List<Boleto> listarBoletosPorPeriodo(Long empresaId, LocalDate inicio, LocalDate fim) {
        logger.info("[BOLETOS_PERIODO] Listando boletos - Empresa: {}, Período: {} a {}", 
                   empresaId, inicio, fim);
        return boletoRepository.findBoletosPorPeriodo(empresaId, inicio, fim);
    }
    
    public Map<String, Object> obterEstatisticas(Long empresaId) {
        logger.info("[ESTATISTICAS_BOLETOS] Calculando estatísticas da empresa: {}", empresaId);
        
        Long pendentes = boletoRepository.countByEmpresaIdAndStatus(empresaId, StatusBoleto.PENDENTE);
        Long pagos = boletoRepository.countByEmpresaIdAndStatus(empresaId, StatusBoleto.PAGO);
        Long vencidos = boletoRepository.countByEmpresaIdAndStatus(empresaId, StatusBoleto.VENCIDO);
        Long cancelados = boletoRepository.countByEmpresaIdAndStatus(empresaId, StatusBoleto.CANCELADO);
        
        return Map.of(
            "pendentes", pendentes,
            "pagos", pagos,
            "vencidos", vencidos,
            "cancelados", cancelados,
            "total", pendentes + pagos + vencidos + cancelados
        );
    }
    
    private void validarContaBancaria(Long empresaId, Long contaBancariaId) {
        try {
            List<Map<String, Object>> contas = companyClient.getContasBancarias(empresaId);
            boolean contaValida = contas.stream()
                    .anyMatch(conta -> conta.get("id").equals(contaBancariaId));
            
            if (!contaValida) {
                throw new RuntimeException("Conta bancária não encontrada para a empresa");
            }
        } catch (Exception e) {
            logger.warn("[VALIDAR_CONTA] Erro ao validar conta bancária: {}", e.getMessage());
            // Em caso de erro na integração, permite continuar
        }
    }
    
    private void gerarCodigosBoleto(Boleto boleto) {
        // Simulação de geração de códigos (em produção usar biblioteca específica do banco)
        String nossoNumero = String.format("%010d", System.currentTimeMillis() % 10000000000L);
        String codigoBarras = "34191" + String.format("%014d", System.currentTimeMillis() % 100000000000000L) + 
                             String.format("%010d", boleto.getValor().multiply(new BigDecimal(100)).longValue()) + 
                             "000";
        String linhaDigitavel = formatarLinhaDigitavel(codigoBarras);
        
        boleto.setNossoNumero(nossoNumero);
        boleto.setCodigoBarras(codigoBarras);
        boleto.setLinhaDigitavel(linhaDigitavel);
    }
    
    private String formatarLinhaDigitavel(String codigoBarras) {
        // Simulação de formatação da linha digitável
        return codigoBarras.substring(0, 5) + "." + 
               codigoBarras.substring(5, 10) + " " +
               codigoBarras.substring(10, 15) + "." +
               codigoBarras.substring(15, 21) + " " +
               codigoBarras.substring(21, 26) + "." +
               codigoBarras.substring(26, 32) + " " +
               codigoBarras.substring(32, 33) + " " +
               codigoBarras.substring(33);
    }
}