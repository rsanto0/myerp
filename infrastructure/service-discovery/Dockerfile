FROM openjdk:17-jdk-slim

LABEL maintainer="MyERP Team"
LABEL service="eureka-server"
LABEL version="1.0.0"

# Instalar curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Criar usuário não-root
RUN groupadd -r myerp && useradd -r -g myerp myerp

# Diretório da aplicação
WORKDIR /app

# Copiar JAR
COPY target/service-discovery-*.jar app.jar

# Alterar proprietário
RUN chown -R myerp:myerp /app

# Usar usuário não-root
USER myerp

# Porta da aplicação
EXPOSE 8761

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8761/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "/app/app.jar"]