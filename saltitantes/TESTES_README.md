# 📁 Estrutura Organizada de Testes

Este projeto possui uma estrutura de testes organizada por **tipo de teste**, facilitando a manutenção, compreensão e execução dos testes.

## 📂 Estrutura de Pastas

```
src/test/java/com/example/saltitantes/
├── sistema/               # Testes de Sistema (E2E com Selenium)
├── funcional/             # Testes de Integração
├── dominio/               # Testes de Unidade (Regras de Negócio)
├── dubles/                # Testes de Unidade (com Mocks)
├── estrutural/            # Testes de Unidade (Estruturais)
├── fronteira/             # Testes de Unidade (Casos Limite)
├── propriedades/          # Testes de Unidade (Property-based)
├── config/                # Configurações de teste
└── SaltitantesApplicationTests_BACKUP.java # Backup do arquivo original
```

## 📊 **RESUMO GERAL**

### **TOTAL: 164 testes executados (107 métodos) distribuídos em:**

| **Tipo de Teste**           | **Métodos** | **Execuções** | **Porcentagem** | **Descrição**                   |
| --------------------------- | ----------- | ------------- | --------------- | ------------------------------- |
| **🔵 Testes de Unidade**    | **80**      | **123**       | **75%**         | Testam componentes isolados     |
| **🟡 Testes de Integração** | **17**      | **36**        | **22%**         | Testam integração entre camadas |
| **🟢 Testes de Sistema**    | **5**       | **5**         | **3%**          | Testam o sistema completo E2E   |
| **⚫ Outros**               | **5**       | **0**         | **0%**          | Arquivo backup (não executado)  |

## 🎯 Tipos de Teste

### 1. **🔵 Testes de Unidade** (`dominio/`, `dubles/`, `estrutural/`, `fronteira/`, `propriedades/`)

**Objetivo**: Testam componentes individuais de forma isolada, garantindo que cada unidade de código funciona corretamente independentemente.

#### **Domínio (26 métodos → 66 execuções)** - Regras de Negócio

**Focam em**: Validação das regras de negócio e restrições fundamentais do sistema.

**Arquivos**:

- **`TesteDominio.java`** (2 métodos → 13 execuções): Validação de limites de criaturas e iterações
- **`TesteClusterDominio.java`** (5 métodos → 8 execuções): Formação e comportamento de clusters
- **`TesteGuardiaDominio.java`** (6 métodos → 18 execuções): Comportamento do guardião
- **`TesteUsuarioDominio.java`** (13 métodos → 27 execuções): Criação, validação e autenticação de usuários

#### **Dublês (3 métodos → 3 execuções)** - Testes com Mocks

**Focam em**: Isolamento de componentes usando dublês de teste.

**Arquivos**:

- **`TesteComDubles.java`** (3 métodos → 3 execuções): Testes usando Mockito para isolar dependências

#### **Estrutural (24 métodos → 30 execuções)** - Interações entre Componentes

**Focam em**: Verificação das interações entre componentes internos do sistema.

**Arquivos**:

- **`TesteEstrutural.java`** (22 métodos → 28 execuções): Interações entre criaturas, cálculo de distância, roubo
- **`UsuarioTest.java`** (2 métodos → 2 execuções): Testes específicos da entidade Usuario

#### **Fronteira (14 métodos → 22 execuções)** - Casos Limite

**Focam em**: Comportamento do sistema em casos extremos e nos limites das regras.

**Arquivos**:

- **`TesteFronteira.java`** (14 métodos → 22 execuções): Valores nos limites, zero iterações, conservação de ouro

#### **Propriedades (7 métodos → 7 execuções)** - Property-Based Testing

**Focam em**: Validação automatizada com dados aleatórios usando jqwik.

**Arquivos**:

- **`TestePropriedades.java`** (7 métodos → 7 execuções): Testes baseados em propriedades

### 2. **🟡 Testes de Integração** (`funcional/`)

**Objetivo**: Validam a integração entre múltiplas camadas do sistema (services, entities, DTOs).

#### **Funcional (17 métodos → 36 execuções)** - Integração entre Camadas

**Focam em**: Execução completa de funcionalidades envolvendo múltiplos componentes.

**Arquivos**:

- **`TesteFuncional.java`** (17 métodos → 36 execuções): Simulação completa, guardião, clusters
  - Usa `@SpringBootTest` para contexto completo do Spring

### 3. **🟢 Testes de Sistema** (`sistema/`)

**Objetivo**: Testam o sistema completo end-to-end através da interface web real.

#### **Sistema (5 métodos → 5 execuções)** - End-to-End com Selenium

**Focam em**: Fluxos completos do usuário através da interface web.

**Arquivos**:

- **`LoginTest.java`** (1 método → 1 execução): Login bem-sucedido
- **`CadastroTest.java`** (1 método → 1 execução): Fluxo de cadastro
- **`JornadaCompletaTest.java`** (1 método → 1 execução): Jornada completa do usuário
- **`LoginJornadaFalhaTest.java`** (1 método → 1 execução): Falha no login
- **`CadastroJornadaFalhaTest.java`** (1 método → 1 execução): Falha no cadastro

**Classes Auxiliares**:

- **PageObjects**: `LoginPageObject.java`, `CadastroPageObject.java`, `SimulationPageObject.java`, `EstatisticasPageObject.java`
- **Configuração**: `WebDriverConfig.java`

## 📊 Resumo dos Testes

| **Categoria**     | **Tipo**       | **Métodos → Execuções** | **Foco Principal**              |
| ----------------- | -------------- | ----------------------- | ------------------------------- |
| **🔵 Unidade**    | Domínio        | 26 → 66                 | Regras de negócio e restrições  |
| **🔵 Unidade**    | Dublês         | 3 → 3                   | Isolamento com mocks            |
| **🔵 Unidade**    | Estrutural     | 24 → 30                 | Interações entre componentes    |
| **🔵 Unidade**    | Fronteira      | 14 → 22                 | Limites e casos extremos        |
| **🔵 Unidade**    | Propriedades   | 7 → 7                   | Property-based testing          |
| **🟡 Integração** | Funcional      | 17 → 36                 | Integração entre camadas        |
| **🟢 Sistema**    | E2E            | 5 → 5                   | Interface completa com Selenium |
| **⚫ Backup**     | Arquivo Backup | 5 → 0                   | Não executado                   |
| **TOTAL**         |                | **107 → 164**           | **Cobertura completa**          |

## 🎯 **TÉCNICAS DE TESTE UTILIZADAS**

### **Técnicas de Teste:**

- ✅ **Testes Parametrizados** (JUnit 5) - Múltiplos cenários automaticamente
- ✅ **Property-Based Testing** (jqwik) - Validação com dados aleatórios
- ✅ **Mocking** (Mockito) - Isolamento de dependências
- ✅ **Integration Testing** (Spring Boot Test) - Teste de múltiplas camadas
- ✅ **E2E Testing** (Selenium) - Teste completo da interface
- ✅ **Boundary Testing** - Casos limite e extremos
- ✅ **Domain Testing** - Regras de negócio específicas

### **Padrões de Qualidade:**

- 📝 **Documentação clara** de cada teste com propósito definido
- 🔍 **Cobertura abrangente** de cenários positivos e negativos
- 🧪 **Isolamento adequado** usando mocks quando necessário
- 📊 **Validação robusta** com AssertJ para assertions expressivas
- 🔄 **Reutilização** através de PageObjects para testes de sistema

## 🚀 Como Executar

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

## ✅ Benefícios da Nova Estrutura

1. **📁 Organização Clara**: Cada tipo de teste em sua pasta específica.
2. **🎯 Propósito Definido**: Cada categoria tem objetivos claros e bem documentados.
3. **🔍 Fácil Manutenção**: Localização rápida de testes por funcionalidade.
4. **📚 Melhor Documentação**: Cada teste possui documentação detalhada do seu propósito.
5. **🧪 Execução Seletiva**: Possibilidade de executar apenas categorias específicas.
6. **📈 Cobertura Organizada**: Visão clara de que aspectos estão sendo testados.
7. **🔄 Escalabilidade**: Estrutura permite fácil adição de novos testes.

## 🔄 Evolução dos Testes

**164 testes organizados** em uma estrutura robusta e escalável:

- ✅ **Funcionalidade preservada**: Todos os testes originais migrados e expandidos.
- ✅ **Cobertura expandida**: Novos testes adicionados para casos específicos.
- ✅ **Documentação melhorada**: Cada teste agora tem documentação clara do seu propósito.
- ✅ **Organização aprimorada**: Estrutura lógica por tipo de teste.
- ✅ **Qualidade assegurada**: 75% testes de unidade, 22% integração, 3% sistema.

---

**🎉 Resultado**: Estrutura de testes mais robusta, organizada e fácil de manter, com cobertura completa do sistema!
