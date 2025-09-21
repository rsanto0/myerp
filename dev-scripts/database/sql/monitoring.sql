-- ========================================
-- Monitoring Module - Dados Iniciais
-- ========================================

-- Configurações de alertas por serviço
INSERT INTO alertas_config (servico, metrica, limite_warning, limite_critical, ativo, email_notificacao) VALUES 
('eureka-server', 'response_time', 1000, 3000, true, 'admin@myerp.com'),
('api-gateway', 'response_time', 500, 2000, true, 'admin@myerp.com'),
('auth-service', 'memory_usage', 80, 95, true, 'admin@myerp.com'),
('rh-module', 'memory_usage', 85, 95, true, 'admin@myerp.com'),
('biometria-module', 'cpu_usage', 70, 90, true, 'admin@myerp.com'),
('company-module', 'disk_usage', 80, 95, true, 'admin@myerp.com'),
('financial-module', 'response_time', 800, 2500, true, 'admin@myerp.com'),
('monitoring-module', 'memory_usage', 75, 90, true, 'admin@myerp.com')
ON CONFLICT (servico, metrica) DO NOTHING;

-- Histórico de métricas (últimos dias)
INSERT INTO metricas_historico (servico, timestamp, cpu_usage, memory_usage, disk_usage, response_time, status) VALUES 
-- Eureka Server
('eureka-server', CURRENT_TIMESTAMP - INTERVAL '1 hour', 15.5, 45.2, 25.0, 150, 'UP'),
('eureka-server', CURRENT_TIMESTAMP - INTERVAL '2 hours', 12.3, 42.8, 25.0, 145, 'UP'),
('eureka-server', CURRENT_TIMESTAMP - INTERVAL '3 hours', 18.7, 48.1, 25.0, 160, 'UP'),
-- API Gateway
('api-gateway', CURRENT_TIMESTAMP - INTERVAL '1 hour', 25.8, 55.4, 30.0, 85, 'UP'),
('api-gateway', CURRENT_TIMESTAMP - INTERVAL '2 hours', 22.1, 52.3, 30.0, 92, 'UP'),
('api-gateway', CURRENT_TIMESTAMP - INTERVAL '3 hours', 28.9, 58.7, 30.0, 78, 'UP'),
-- Auth Service
('auth-service', CURRENT_TIMESTAMP - INTERVAL '1 hour', 20.4, 38.9, 20.0, 120, 'UP'),
('auth-service', CURRENT_TIMESTAMP - INTERVAL '2 hours', 18.2, 35.6, 20.0, 115, 'UP'),
('auth-service', CURRENT_TIMESTAMP - INTERVAL '3 hours', 22.7, 41.2, 20.0, 125, 'UP'),
-- RH Module
('rh-module', CURRENT_TIMESTAMP - INTERVAL '1 hour', 30.1, 62.5, 35.0, 200, 'UP'),
('rh-module', CURRENT_TIMESTAMP - INTERVAL '2 hours', 28.5, 59.8, 35.0, 195, 'UP'),
('rh-module', CURRENT_TIMESTAMP - INTERVAL '3 hours', 32.8, 65.2, 35.0, 210, 'UP'),
-- Biometria Module
('biometria-module', CURRENT_TIMESTAMP - INTERVAL '1 hour', 45.2, 70.3, 40.0, 300, 'UP'),
('biometria-module', CURRENT_TIMESTAMP - INTERVAL '2 hours', 42.8, 68.1, 40.0, 285, 'UP'),
('biometria-module', CURRENT_TIMESTAMP - INTERVAL '3 hours', 48.5, 72.9, 40.0, 320, 'UP')
ON CONFLICT DO NOTHING;

-- Status dos serviços
INSERT INTO status_servicos (servico, porta, url_health, status, ultimo_check, uptime_segundos, versao) VALUES 
('eureka-server', 8761, 'http://localhost:8761/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('api-gateway', 8080, 'http://localhost:8080/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('auth-service', 8081, 'http://localhost:8081/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('rh-module', 8082, 'http://localhost:8082/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('biometria-module', 8083, 'http://localhost:8083/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('monitoring-module', 8084, 'http://localhost:8084/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('company-module', 8085, 'http://localhost:8085/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0'),
('financial-module', 8086, 'http://localhost:8086/actuator/health', 'UP', CURRENT_TIMESTAMP, 86400, '1.0.0')
ON CONFLICT (servico) DO NOTHING;

-- Incidentes registrados
INSERT INTO incidentes (servico, tipo, descricao, severidade, status, data_inicio, data_resolucao) VALUES 
('auth-service', 'PERFORMANCE', 'Tempo de resposta elevado', 'WARNING', 'RESOLVIDO', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '30 minutes'),
('biometria-module', 'AVAILABILITY', 'Serviço indisponível por 5 minutos', 'CRITICAL', 'RESOLVIDO', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '5 minutes'),
('financial-module', 'MEMORY', 'Uso de memória acima de 90%', 'WARNING', 'RESOLVIDO', CURRENT_TIMESTAMP - INTERVAL '6 hours', CURRENT_TIMESTAMP - INTERVAL '5 hours')
ON CONFLICT DO NOTHING;

-- Configurações de monitoramento
INSERT INTO configuracoes_monitoring (chave, valor, descricao) VALUES 
('CHECK_INTERVAL_SECONDS', '30', 'Intervalo entre verificações de saúde'),
('RETENTION_DAYS', '30', 'Dias para manter histórico de métricas'),
('EMAIL_ALERTS', 'true', 'Enviar alertas por email'),
('SLACK_WEBHOOK', '', 'URL do webhook do Slack para alertas'),
('MAX_RESPONSE_TIME', '5000', 'Tempo máximo de resposta em ms'),
('DASHBOARD_REFRESH', '10', 'Intervalo de refresh do dashboard em segundos')
ON CONFLICT (chave) DO NOTHING;