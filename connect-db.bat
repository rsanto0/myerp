@echo off
echo Conectando ao PostgreSQL...
echo.
echo Comandos uteis:
echo \l          - Listar databases
echo \c auth_db  - Conectar ao database auth_db
echo \dt         - Listar tabelas
echo \d users    - Descrever tabela users
echo SELECT * FROM users; - Ver dados da tabela
echo \q          - Sair
echo.
docker exec -it myerp-postgres psql -U myerp_user -d myerp_db