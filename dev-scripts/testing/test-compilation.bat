@echo off
echo ========================================
echo    TESTANDO COMPILACAO - MyERP
echo ========================================
echo.

echo [1/9] Testando Eureka Server...
cd /d "%~dp0\..\infrastructure\service-discovery"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Eureka Server falhou na compilacao
    goto :error
) else (
    echo ✅ Eureka Server compilado com sucesso
)

echo.
echo [2/9] Testando Config Server...
cd /d "%~dp0\..\infrastructure\config-server"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Config Server falhou na compilacao
    goto :error
) else (
    echo ✅ Config Server compilado com sucesso
)

echo.
echo [3/9] Testando API Gateway...
cd /d "%~dp0\..\infrastructure\api-gateway"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: API Gateway falhou na compilacao
    goto :error
) else (
    echo ✅ API Gateway compilado com sucesso
)

echo.
echo [4/9] Testando Auth Service...
cd /d "%~dp0\..\infrastructure\auth-service"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Auth Service falhou na compilacao
    goto :error
) else (
    echo ✅ Auth Service compilado com sucesso
)

echo.
echo [5/9] Testando RH Module...
cd /d "%~dp0\..\modules\rh-module"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: RH Module falhou na compilacao
    goto :error
) else (
    echo ✅ RH Module compilado com sucesso
)

echo.
echo [6/9] Testando Biometria Module...
cd /d "%~dp0\..\modules\biometria-module"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Biometria Module falhou na compilacao
    goto :error
) else (
    echo ✅ Biometria Module compilado com sucesso
)

echo.
echo [7/9] Testando Monitoring Module...
cd /d "%~dp0\..\modules\monitoring-module"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Monitoring Module falhou na compilacao
    goto :error
) else (
    echo ✅ Monitoring Module compilado com sucesso
)

echo.
echo [8/9] Testando Company Module...
cd /d "%~dp0\..\modules\company-module"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Company Module falhou na compilacao
    goto :error
) else (
    echo ✅ Company Module compilado com sucesso
)

echo.
echo [9/9] Testando Financial Module...
cd /d "%~dp0\..\modules\financial-module"
mvn clean compile -q
if %errorlevel% neq 0 (
    echo ❌ ERRO: Financial Module falhou na compilacao
    goto :error
) else (
    echo ✅ Financial Module compilado com sucesso
)

echo.
echo ========================================
echo   ✅ TODOS OS MODULOS COMPILARAM!
echo ========================================
echo.
echo 🎉 Sistema MyERP pronto para execucao:
echo    - 4 modulos de infraestrutura
echo    - 5 modulos de negocio
echo    - 9 modulos totais funcionais
echo.
echo Execute: dev-scripts/start-myerp.bat
echo.
goto :end

:error
echo.
echo ========================================
echo   ❌ ERRO NA COMPILACAO!
echo ========================================
echo.
echo Verifique os logs acima para detalhes.
echo.

:end
cd /d "%~dp0"
pause