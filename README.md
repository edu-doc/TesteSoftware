# 📄 Documentação do Projeto Saltitantes

## 🏆 Descrição

**Saltitantes** é um projeto Java que simula um mundo habitado por criaturas que se movem, interagem e roubam ouro umas das outras. O objetivo do projeto é desenvolver uma aplicação com base nos requisitos informados e garantir a qualidade do software por meio de testes automatizados.

## 🚀 Tecnologias Utilizadas

- Java 17+
- Maven
- JUnit 5 (Jupiter)
- AssertJ
- Spring Boot

## 🧪 Testes Automatizados

### Tecnologias Utilizadas

- **JUnit 5** (Jupiter) com testes parametrizados e property-based testing
- **AssertJ** para assertions fluentes
- **Mockito** para criação de dublês de teste
- **Selenium WebDriver** para testes de sistema E2E
- **Spring Boot Test** para testes de integração
- **jqwik** para testes baseados em propriedades

### Estrutura dos Testes

O projeto possui uma estrutura organizada de testes distribuída em diferentes categorias:

```
src/test/java/com/example/saltitantes/
├── sistema/          # Testes de Sistema (E2E com Selenium)
├── funcional/        # Testes de Integração
├── dominio/          # Testes de Unidade (Regras de Negócio)
├── dubles/           # Testes de Unidade (com Mocks)
├── estrutural/       # Testes de Unidade (Estruturais)
├── fronteira/        # Testes de Unidade (Casos Limite)
├── propriedades/     # Testes de Unidade (Property-based)
└── config/           # Configurações de teste
```

## 📊 **RESUMO GERAL DOS TESTES**

### **TOTAL: 164 testes executados (107 métodos) distribuídos em:**

| **Tipo de Teste**           | **Métodos** | **Execuções** | **Porcentagem** | **Descrição**                   |
| --------------------------- | ----------- | ------------- | --------------- | ------------------------------- |
| **🔵 Testes de Unidade**    | **80**      | **123**       | **75%**         | Testam componentes isolados     |
| **🟡 Testes de Integração** | **17**      | **36**        | **22%**         | Testam integração entre camadas |
| **🟢 Testes de Sistema**    | **5**       | **5**         | **3%**          | Testam o sistema completo E2E   |
| **⚫ Outros (Backup)**      | **5**       | **0**         | **0%**          | Arquivo backup (não executado)  |

---

## 📂 **DETALHAMENTO POR CATEGORIA**

### **🔵 Testes de Unidade (80 métodos → 123 execuções)**

#### **Domínio (26 métodos → 66 execuções)** - Regras de Negócio

- **`TesteDominio.java`**: 2 métodos → 13 execuções
  - Validação de limites de criaturas e iterações
  - Restrições fundamentais do sistema
- **`TesteClusterDominio.java`**: 5 métodos → 8 execuções
  - Formação e comportamento de clusters
  - Soma de ouro e movimentação
- **`TesteGuardiaDominio.java`**: 6 métodos → 18 execuções
  - Comportamento do guardião
  - Absorção de ouro e movimento
- **`TesteUsuarioDominio.java`**: 13 métodos → 27 execuções
  - Criação e validação de usuários
  - Autenticação e gestão de pontuação

#### **Dublês (3 métodos → 3 execuções)** - Testes com Mocks

- **`TesteComDubles.java`**: 3 métodos → 3 execuções
  - Isolamento de componentes usando Mockito
  - Verificação de interações entre serviços

#### **Estrutural (24 métodos → 30 execuções)** - Interações entre Componentes

- **`TesteEstrutural.java`**: 22 métodos → 28 execuções
  - Interações entre criaturas (roubo, distância)
  - Validação de parâmetros e estado interno
- **`UsuarioTest.java`**: 2 métodos → 2 execuções
  - Testes específicos da entidade Usuario

#### **Fronteira (14 métodos → 22 execuções)** - Casos Limite

- **`TesteFronteira.java`**: 14 métodos → 22 execuções
  - Valores nos limites exatos (mínimo/máximo)
  - Comportamento com zero iterações
  - Conservação de ouro em cenários extremos

#### **Propriedades (7 métodos → 7 execuções)** - Property-Based Testing

- **`TestePropriedades.java`**: 7 métodos → 7 execuções
  - Testes baseados em propriedades usando jqwik
  - Validação automatizada com dados aleatórios

### **🟡 Testes de Integração (17 métodos → 36 execuções)**

#### **Funcional (17 métodos → 36 execuções)** - Integração entre Camadas

- **`TesteFuncional.java`**: 17 métodos → 36 execuções
  - Execução completa da simulação
  - Funcionamento do guardião e clusters
  - Integração entre services, entities e DTOs
  - Usa `@SpringBootTest` para contexto completo

### **🟢 Testes de Sistema (5 métodos → 5 execuções)**

#### **Sistema (5 métodos → 5 execuções)** - End-to-End com Interface Web

- **`LoginTest.java`**: 1 teste - Login bem-sucedido
- **`CadastroTest.java`**: 1 teste - Fluxo de cadastro
- **`JornadaCompletaTest.java`**: 1 teste - Jornada completa do usuário
- **`LoginJornadaFalhaTest.java`**: 1 teste - Falha no login
- **`CadastroJornadaFalhaTest.java`**: 1 teste - Falha no cadastro

Todos usam **Selenium WebDriver** para testar a interface web completa, simulando interações reais do usuário.

---

## 🎯 **CARACTERÍSTICAS DOS TESTES**

### **Técnicas Utilizadas:**

- ✅ **Testes Parametrizados** - Múltiplos cenários automaticamente
- ✅ **Property-Based Testing** - Validação com dados aleatórios
- ✅ **Mocking** - Isolamento de dependências
- ✅ **Integration Testing** - Teste de múltiplas camadas
- ✅ **E2E Testing** - Teste completo da interface
- ✅ **Boundary Testing** - Casos limite e extremos
- ✅ **Domain Testing** - Regras de negócio específicas

### **Qualidade dos Testes:**

- 📝 **Documentação clara** de cada teste com propósito definido
- 🔍 **Cobertura abrangente** de cenários positivos e negativos
- 🧪 **Isolamento adequado** usando mocks quando necessário
- 📊 **Validação robusta** com AssertJ para assertions expressivas
- 🔄 **Reutilização** através de PageObjects para testes de sistema

## 🚀 Como Executar os Testes

### Executar todos os testes:

```bash
mvn test
```

### Executar apenas uma categoria:

```bash
# Testes de sistema (E2E)
mvn test -Dtest="com.example.saltitantes.sistema.*"

# Testes de integração
mvn test -Dtest="com.example.saltitantes.funcional.*"

# Testes de unidade - domínio
mvn test -Dtest="com.example.saltitantes.dominio.*"

# Testes de unidade - estrutural
mvn test -Dtest="com.example.saltitantes.estrutural.*"

# Testes de unidade - fronteira
mvn test -Dtest="com.example.saltitantes.fronteira.*"

# Testes de unidade - propriedades
mvn test -Dtest="com.example.saltitantes.propriedades.*"

# Testes de unidade - dublês
mvn test -Dtest="com.example.saltitantes.dubles.*"
```

### Executar um teste específico:

```bash
mvn test -Dtest="TesteDominio#testInicializacaoRegrasNegocio"
```

## ✅ Benefícios da Estrutura de Testes

1. **📁 Organização Clara**: Cada tipo de teste em sua categoria específica
2. **🎯 Cobertura Completa**: 164 testes executados (107 métodos) cobrindo unidade, integração e sistema
3. **🔍 Fácil Manutenção**: Localização rápida de testes por funcionalidade
4. **📚 Documentação Detalhada**: Cada teste possui documentação clara do seu propósito
5. **🧪 Execução Seletiva**: Possibilidade de executar apenas categorias específicas
6. **📈 Qualidade Assegurada**: Cobertura robusta com diferentes técnicas de teste

## 📝 Critérios de Qualidade e Validação

- ✔️ **75% Testes de Unidade**: Garantem que cada componente funciona isoladamente
- ✔️ **22% Testes de Integração**: Validam a comunicação entre componentes
- ✔️ **3% Testes de Sistema**: Asseguram que o sistema funciona de ponta a ponta
- ✔️ **164 testes executados**: Cobertura completa com diferentes técnicas
- ✔️ **Nenhuma exceção não tratada** durante a simulação
- ✔️ **Consistência do total de ouro** em todas as operações
- ✔️ **Garantia de movimentação válida** dentro dos limites do sistema
- ✔️ **Cobertura abrangente** de cenários positivos e negativos
