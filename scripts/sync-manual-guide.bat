@echo off
echo ========================================
echo    SINCRONIZAÇÃO MANUAL POSTMAN
echo ========================================
echo.

echo 📋 PASSO A PASSO MANUAL:
echo.
echo 1. Abra o Postman
echo 2. Import → Upload Files
echo 3. Selecione: postman\MyERP-System.postman_collection.json
echo 4. Selecione: postman\MyERP-Environment.postman_environment.json
echo 5. Pronto! Collections importadas
echo.
echo 🔄 PARA SINCRONIZAR MUDANÇAS:
echo.
echo 1. Faça alterações no Postman
echo 2. Export → Collection → Overwrite
echo 3. Export → Environment → Overwrite
echo 4. Commit no Git: git add postman/ && git commit -m "Update collections"
echo.
echo 🌐 PARA COMPARTILHAR:
echo.
echo 1. Postman → Collection → Share
echo 2. Get public link
echo 3. Compartilhe o link da collection
echo.
echo 💡 DICA: Use o Postman Desktop para melhor experiência
echo.
pause