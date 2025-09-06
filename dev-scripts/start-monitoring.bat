@echo off
echo ========================================
echo    MyERP - Iniciando Modulo Monitoramento
echo ========================================
echo.

echo [INFO] Verificando se Eureka esta disponivel...
ping 127.0.0.1 -n 3 >nul

echo [INFO] Aguardando Eureka Server estar pronto...
ping 127.0.0.1 -n 11 >nul

echo [INFO] Iniciando Monitoring Module na porta 8084...
echo Profile: local (usando application.yml)
cd /d "c:\Area_de_Tecnologia\wksp-eclipse\myErp\dev-scripts"
cd ..\modules\monitoring-module
start "Monitoring Module" cmd /k "mvn spring-boot:run -Dspring.profiles.active=local"

echo.
echo ========================================
echo   Monitoring Module iniciado!
echo   Dashboard: http://localhost:8084
echo ========================================
echo.
pause