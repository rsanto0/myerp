@echo off
echo Habilitando Hyper-V...
echo IMPORTANTE: Execute como Administrador!
echo.

dism /online /enable-feature /featurename:Microsoft-Hyper-V-All /all /norestart
dism /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

echo.
echo Hyper-V habilitado! Reinicie o computador.
pause