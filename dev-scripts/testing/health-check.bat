@echo off
echo ========================================
echo    HEALTH CHECK - SISTEMA MyERP
echo ========================================
echo.

echo Verificando saúde de todos os serviços...
echo.

echo [1/8] Eureka Server (8761)
curl -s http://localhost:8761/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [2/8] Config Server (8888)
curl -s http://localhost:8888/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [3/8] API Gateway (8080)
curl -s http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [4/8] Auth Service (8081)
curl -s http://localhost:8081/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [5/8] RH Module (8082)
curl -s http://localhost:8082/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [6/8] Biometria Module (8083)
curl -s http://localhost:8083/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [7/8] Company Module (8085)
curl -s http://localhost:8085/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo [8/8] Financial Module (8086)
curl -s http://localhost:8086/actuator/health >nul 2>&1
if %errorlevel% equ 0 (echo ✅ UP) else (echo ❌ DOWN)

echo.
echo ========================================
echo Dashboard completo: http://localhost:8084/api/monitoring/dashboard
echo ========================================
pause