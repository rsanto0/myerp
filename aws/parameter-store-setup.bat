@echo off
REM Script para configurar AWS Parameter Store - Windows

set ENVIRONMENT=%1
set REGION=%2

if "%ENVIRONMENT%"=="" set ENVIRONMENT=prod
if "%REGION%"=="" set REGION=us-east-1

echo Configurando Parameter Store para ambiente: %ENVIRONMENT%
echo Regiao: %REGION%
echo.

REM Database Parameters
aws ssm put-parameter ^
    --name "/myerp/%ENVIRONMENT%/database/port" ^
    --value "5432" ^
    --type "String" ^
    --region %REGION% ^
    --overwrite

aws ssm put-parameter ^
    --name "/myerp/%ENVIRONMENT%/database/name" ^
    --value "myerp_db" ^
    --type "String" ^
    --region %REGION% ^
    --overwrite

REM Service URLs
aws ssm put-parameter ^
    --name "/myerp/%ENVIRONMENT%/services/eureka/url" ^
    --value "http://eureka.%ENVIRONMENT%.myerp.internal:8761/eureka/" ^
    --type "String" ^
    --region %REGION% ^
    --overwrite

aws ssm put-parameter ^
    --name "/myerp/%ENVIRONMENT%/services/auth/url" ^
    --value "http://auth.%ENVIRONMENT%.myerp.internal:8081" ^
    --type "String" ^
    --region %REGION% ^
    --overwrite

REM Application Configuration
aws ssm put-parameter ^
    --name "/myerp/%ENVIRONMENT%/logging/level" ^
    --value "INFO" ^
    --type "String" ^
    --region %REGION% ^
    --overwrite

echo.
echo Parameter Store configurado com sucesso!
echo.
echo Verifique os parametros criados:
echo aws ssm get-parameters-by-path --path /myerp/%ENVIRONMENT% --recursive --region %REGION%