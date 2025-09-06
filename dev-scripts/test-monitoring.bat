@echo off
echo ========================================
echo  TESTE DO MODULO DE MONITORAMENTO
echo ========================================
echo.

echo [INFO] Testando endpoints de health dos servicos...
echo.

echo --- Eureka Server (8761) ---
curl -s http://localhost:8761/actuator/health 2>nul && echo [OK] Eureka UP || echo [ERRO] Eureka DOWN
echo.

echo --- API Gateway (8080) ---
curl -s http://localhost:8080/actuator/health 2>nul && echo [OK] Gateway UP || echo [ERRO] Gateway DOWN
echo.

echo --- Auth Service (8081) ---
curl -s http://localhost:8081/actuator/health 2>nul && echo [OK] Auth UP || echo [ERRO] Auth DOWN
echo.

echo --- RH Module (8082) ---
curl -s http://localhost:8082/actuator/health 2>nul && echo [OK] RH UP || echo [ERRO] RH DOWN
echo.

echo --- Biometria Module (8083) ---
curl -s http://localhost:8083/actuator/health 2>nul && echo [OK] Biometria UP || echo [ERRO] Biometria DOWN
echo.

echo --- Monitoring Module (8084) ---
curl -s http://localhost:8084/actuator/health 2>nul && echo [OK] Monitoring UP || echo [ERRO] Monitoring DOWN
echo.

echo ========================================
echo  DASHBOARD DO MONITORAMENTO
echo ========================================
echo.

echo [INFO] Acessando dashboard completo...
curl -s http://localhost:8084/api/monitoring/dashboard
echo.
echo.

echo [INFO] Status dos servicos:
curl -s http://localhost:8084/api/monitoring/services
echo.
echo.

echo ========================================
echo  TESTE CONCLUIDO
echo ========================================
echo.
echo Para acessar o dashboard web: http://localhost:8084
echo Para ver metricas: http://localhost:8084/api/monitoring/metrics
echo.
pause