@echo off
echo ========================================
echo   VERIFICANDO CONFIGURACAO DE BANCO
echo ========================================
echo.

echo [INFO] Verificando configuracoes de banco de dados...
echo.

echo [1/6] Auth Service:
findstr /C:"postgresql" "..\infrastructure\auth-service\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ✅ PostgreSQL configurado
) else (
    echo ❌ PostgreSQL NAO configurado
)

echo.
echo [2/6] RH Module:
findstr /C:"postgresql" "..\modules\rh-module\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ✅ PostgreSQL configurado
) else (
    echo ❌ PostgreSQL NAO configurado
)

echo.
echo [3/6] Biometria Module:
findstr /C:"postgresql" "..\modules\biometria-module\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ✅ PostgreSQL configurado
) else (
    echo ❌ PostgreSQL NAO configurado
)

echo.
echo [4/6] Company Module:
findstr /C:"postgresql" "..\modules\company-module\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ✅ PostgreSQL configurado
) else (
    echo ❌ PostgreSQL NAO configurado
)

echo.
echo [5/6] Financial Module:
findstr /C:"postgresql" "..\modules\financial-module\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ✅ PostgreSQL configurado
) else (
    echo ❌ PostgreSQL NAO configurado
)

echo.
echo [6/6] Monitoring Module:
if exist "..\modules\monitoring-module\src\main\resources\application.yml" (
    findstr /C:"postgresql" "..\modules\monitoring-module\src\main\resources\application.yml" >nul
    if %errorlevel% equ 0 (
        echo ✅ PostgreSQL configurado
    ) else (
        echo ⚠️ Monitoring nao usa banco (OK)
    )
) else (
    echo ⚠️ Monitoring nao usa banco (OK)
)

echo.
echo ========================================
echo   VERIFICACAO CONCLUIDA
echo ========================================
echo.
echo [INFO] Verificando se H2 ainda esta sendo usado...
echo.

findstr /C:"h2" "..\infrastructure\auth-service\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ❌ Auth Service ainda tem configuracao H2
) else (
    echo ✅ Auth Service sem H2
)

findstr /C:"h2" "..\modules\rh-module\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ❌ RH Module ainda tem configuracao H2
) else (
    echo ✅ RH Module sem H2
)

findstr /C:"h2" "..\modules\financial-module\src\main\resources\application.yml" >nul
if %errorlevel% equ 0 (
    echo ❌ Financial Module ainda tem configuracao H2
) else (
    echo ✅ Financial Module sem H2
)

echo.
echo ========================================
echo   TODOS OS MODULOS VERIFICADOS
echo ========================================
pause