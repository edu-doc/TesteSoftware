# Usa imagem base com Java 17
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo .jar gerado para dentro do container
COPY saltitantes/target/saltitantes-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão usada por Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
