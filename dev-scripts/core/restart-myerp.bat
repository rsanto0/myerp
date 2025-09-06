@echo off
echo ========================================
echo    REINICIANDO SISTEMA MyERP
echo ========================================
echo.

echo [1/2] Parando sistema...
call stop-myerp.bat

echo.
echo [2/2] Iniciando sistema...
call start-myerp.bat

echo.
echo ✅ Sistema MyERP reiniciado com sucesso!
pause