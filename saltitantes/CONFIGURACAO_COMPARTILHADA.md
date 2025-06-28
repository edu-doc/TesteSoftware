# 🔐 Guia de Configuração para Projetos Compartilhados

## 📋 Configuração Segura de Credenciais

### 1️⃣ **Configuração Local (Recomendado)**

**Passo 1:** Copie o arquivo exemplo

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

**Passo 2:** Edite suas credenciais em `application-local.properties`

```properties
spring.datasource.username=seu_usuario_real
spring.datasource.password=sua_senha_real
```

**Passo 3:** Execute o projeto

```bash
# Windows (PowerShell) - COMANDO CORRETO
$env:SPRING_PROFILES_ACTIVE="local"; mvn spring-boot:run

# Linux/Mac/CMD
mvn spring-boot:run -Dspring.profiles.active=local
```

### 2️⃣ **Usando Variáveis de Ambiente**

**Configure as variáveis:**

```bash
# Windows (PowerShell)
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"
$env:DB_URL="jdbc:postgresql://localhost:5432/saltitantes_db"

# Linux/Mac
export DB_USERNAME="seu_usuario"
export DB_PASSWORD="sua_senha"
export DB_URL="jdbc:postgresql://localhost:5432/saltitantes_db"
```

**Execute normalmente:**

```bash
mvn spring-boot:run
```

## 🛡️ **Perfis Disponíveis**

| Perfil     | Comando (Windows PowerShell)                           | Comando (Linux/Mac/CMD)                              | Uso                                |
| ---------- | ------------------------------------------------------ | ---------------------------------------------------- | ---------------------------------- |
| **padrão** | `mvn spring-boot:run`                                  | `mvn spring-boot:run`                                | Produção com variáveis de ambiente |
| **local**  | `mvn spring-boot:run "-Dspring.profiles.active=local"` | `mvn spring-boot:run -Dspring.profiles.active=local` | Desenvolvimento com arquivo local  |

## ⚠️ **Regras de Segurança**

✅ **PODE versionar:**

- `application.properties` (sem credenciais reais)
- `application-local.properties.example` (template)

❌ **NUNCA versione:**

- `application-local.properties` (suas credenciais reais)
- `application-production.properties`
- Arquivos `.env`

## 🔧 **Configuração da Equipe**

**Para novos desenvolvedores:**

1. Clone o repositório
2. Copie: `application-local.properties.example` → `application-local.properties`
3. Configure suas credenciais locais
4. Execute:
   - **Windows PowerShell:** `mvn spring-boot:run "-Dspring.profiles.active=local"`
   - **Linux/Mac/CMD:** `mvn spring-boot:run -Dspring.profiles.active=local`

**Para CI/CD:**

- Configure variáveis de ambiente no pipeline
- Use o perfil padrão (sem `-Dspring.profiles.active`)

## 📊 **Resumo das Configurações**

```
application.properties              ← Produção (variáveis de ambiente)
application-local.properties       ← Local (suas credenciais) - NÃO VERSIONAR
application-local.properties.example ← Template para compartilhar
```

## 🚀 **Próximos Passos**

1. Copie o arquivo `.example` para `.properties`
2. Configure suas credenciais
3. Execute com perfil `local`
4. Compartilhe apenas os templates com a equipe!
