@echo off
echo ========================================
echo    BACKUP DE CONFIGURAÇÕES MyERP
echo ========================================
echo.

set BACKUP_DIR=backup-configs-%date:~6,4%%date:~3,2%%date:~0,2%-%time:~0,2%%time:~3,2%%time:~6,2%
set BACKUP_DIR=%BACKUP_DIR: =0%

echo Criando backup em: %BACKUP_DIR%
mkdir "%BACKUP_DIR%" 2>nul

echo [1/5] Backup Docker Compose...
xcopy "..\..\docker-compose.yml" "%BACKUP_DIR%\" /Y >nul

echo [2/5] Backup Config Server...
xcopy "..\..\infrastructure\config-server\src\main\resources\config-repo\*.yml" "%BACKUP_DIR%\config-repo\" /Y >nul

echo [3/5] Backup Auth Service...
xcopy "..\..\infrastructure\auth-service\src\main\resources\*.yml" "%BACKUP_DIR%\auth-service\" /Y >nul

echo [4/5] Backup Módulos...
xcopy "..\..\modules\*\src\main\resources\application.yml" "%BACKUP_DIR%\modules\" /Y /S >nul

echo [5/5] Backup Scripts...
xcopy "..\*.bat" "%BACKUP_DIR%\scripts\" /Y /S >nul

echo.
echo ✅ Backup criado com sucesso em: %BACKUP_DIR%
echo.
pause