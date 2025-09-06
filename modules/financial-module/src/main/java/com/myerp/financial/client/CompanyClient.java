package com.myerp.financial.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para integração com o Company Module.
 * 
 * <p><b>PROCESSO DE NEGÓCIO:</b> Esta classe faz parte do processo de <b>Gestão Financeira Multi-tenant</b>
 * e trabalha em conjunto com o <b>Company Module (8085)</b> para obter dados bancários
 * das empresas antes de processar transações financeiras.
 * 
 * <p><b>INTEGRAÇÕES:</b>
 * <ul>
 *   <li><b>Company Module (8085)</b>: Consome APIs de contas bancárias e dados empresariais</li>
 *   <li><b>Financial Module (8086)</b>: Usado pelos serviços {@code BoletoService} e {@code PixService}</li>
 *   <li><b>Service Discovery (8761)</b>: Descoberta automática do serviço company-service</li>
 *   <li><b>API Gateway (8080)</b>: Roteamento transparente entre módulos</li>
 * </ul>
 * 
 * <p><b>FUNCIONALIDADES:</b>
 * <ul>
 *   <li><b>Validação de contas</b>: Verifica se conta bancária existe e está ativa</li>
 *   <li><b>Dados empresariais</b>: Obtém informações da empresa para transações</li>
 *   <li><b>Filtros especializados</b>: Contas habilitadas para boletos ou PIX</li>
 *   <li><b>Conta principal</b>: Identifica conta padrão da empresa</li>
 * </ul>
 * 
 * <p><b>FLUXO DE USO:</b>
 * <ol>
 *   <li>Usuário cria boleto/PIX via {@code BoletoController} ou {@code PixController}</li>
 *   <li>Serviço financeiro chama {@code getContasBancarias()} para validar</li>
 *   <li>Company Module retorna dados da conta bancária</li>
 *   <li>Transação é processada com dados validados</li>
 * </ol>
 * 
 * <p><b>TRATAMENTO DE ERRO:</b>
 * Em caso de falha na comunicação, os serviços financeiros permitem continuar
 * a operação com log de warning, evitando bloqueio de transações críticas.
 * 
 * @author MyERP Team
 * @version 1.0
 * @since 1.0
 * @see com.myerp.financial.service.BoletoService
 * @see com.myerp.financial.service.PixService
 * @see com.myerp.company.controller.ContaBancariaController
 */
@FeignClient(name = "company-service", url = "http://localhost:8085")
public interface CompanyClient {
    
    @GetMapping("/api/company/contas-bancarias/empresa/{empresaId}")
    List<Map<String, Object>> getContasBancarias(@PathVariable Long empresaId);
    
    @GetMapping("/api/company/contas-bancarias/empresa/{empresaId}/principal")
    Map<String, Object> getContaPrincipal(@PathVariable Long empresaId);
    
    @GetMapping("/api/company/contas-bancarias/empresa/{empresaId}/boletos")
    List<Map<String, Object>> getContasComBoletos(@PathVariable Long empresaId);
    
    @GetMapping("/api/company/contas-bancarias/empresa/{empresaId}/pix")
    List<Map<String, Object>> getContasComPix(@PathVariable Long empresaId);
    
    @GetMapping("/api/company/empresas/{empresaId}")
    Map<String, Object> getEmpresa(@PathVariable Long empresaId);
}