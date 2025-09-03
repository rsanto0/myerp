@echo off
echo ========================================
echo      PARANDO SISTEMA MyERP
echo ========================================
echo.

echo Parando servicos Spring Boot...
taskkill /f /im java.exe 2>nul
echo Servicos Spring Boot parados.

echo.
echo Parando PostgreSQL...
docker-compose down
echo PostgreSQL parado.

echo.
echo ========================================
echo     SISTEMA MyERP PARADO!
echo ========================================
pause