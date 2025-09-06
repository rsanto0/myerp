@echo off
echo ========================================
echo    TESTANDO PROFILES MyERP
echo ========================================
echo.

echo [TESTE] Verificando arquivos de configuracao...
echo.

echo Auth Service:
if exist "..\infrastructure\auth-service\src\main\resources\application.yml" (
    echo ✓ application.yml encontrado
) else (
    echo ✗ application.yml NAO encontrado
)

if exist "..\infrastructure\auth-service\src\main\resources\application-local.yml" (
    echo ✓ application-local.yml encontrado
) else (
    echo ✗ application-local.yml NAO encontrado
)

echo.
echo RH Module:
if exist "..\modules\rh-module\src\main\resources\application.yml" (
    echo ✓ application.yml encontrado
) else (
    echo ✗ application.yml NAO encontrado
)

if exist "..\modules\rh-module\src\main\resources\application-local.yml" (
    echo ✓ application-local.yml encontrado
) else (
    echo ✗ application-local.yml NAO encontrado
)

echo.
echo Biometria Module:
if exist "..\modules\biometria-module\src\main\resources\application.yml" (
    echo ✓ application.yml encontrado
) else (
    echo ✗ application.yml NAO encontrado
)

if exist "..\modules\biometria-module\src\main\resources\application-local.yml" (
    echo ✓ application-local.yml encontrado
) else (
    echo ✗ application-local.yml NAO encontrado
)

echo.
echo Monitoring Module:
if exist "..\modules\monitoring-module\src\main\resources\application.yml" (
    echo ✓ application.yml encontrado
) else (
    echo ✗ application.yml NAO encontrado
)

if exist "..\modules\monitoring-module\src\main\resources\application-local.yml" (
    echo ✓ application-local.yml encontrado
) else (
    echo ✗ application-local.yml NAO encontrado
)

echo.
echo ========================================
echo    TESTE CONCLUIDO!
echo ========================================
pause