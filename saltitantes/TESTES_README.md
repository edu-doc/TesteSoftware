# 📁 Estrutura Organizada de Testes

Este projeto agora possui uma estrutura de testes organizada por **tipo de teste**, facilitando a manutenção, compreensão e execução dos testes.

## 📂 Estrutura de Pastas

```
src/test/java/com/example/saltitantes/
├── dominio/               # Testes de Regras de Negócio
├── duplas/                # Testes com Dublês (Mocks)
├── estrutural/            # Testes Estruturais
├── fronteira/             # Testes de Fronteira
├── funcional/             # Testes Funcionais
└── SaltitantesApplicationTests_BACKUP.java # Backup do arquivo original
```

## 🎯 Tipos de Teste

### 1. **Testes de Domínio** (`dominio/`)

**Objetivo**: Validam as regras de negócio e as restrições fundamentais do sistema, garantindo que os dados de entrada estejam dentro dos limites esperados.

**Focam em**:

- Limites de entrada para a simulação (criaturas, iterações).
- Condições de operação válidas.
- Comportamento do sistema com dados inválidos.

**Testes Incluídos**:

- `testInicializacaoRegrasNegocio()`: Valida limites para o número de criaturas (entre 2 e 1000).
- `testIteracoesRegrasNegocio()`: Valida limites para o número de iterações (entre 1 e 1000).
- `testCriaturasForaDoLimite()`: Garante que o sistema rejeita valores fora da faixa permitida.

**Total**: 13 testes

---

### 2. **Testes de Fronteira** (`fronteira/`)

**Objetivo**: Verificam o comportamento do sistema em casos extremos e nos limites exatos das regras de negócio.

**Focam em**:

- Valores nos limites exatos dos intervalos (mínimo e máximo).
- Comportamento com zero iterações.
- Simulações com o número mínimo de criaturas.
- Conservação de ouro em cenários extremos.

**Testes Incluídos**:

- `testValoresExatosDeFronteira()`: Testa o comportamento com 1, 2, 1000 e 1001 criaturas.
- `testIteracaoZeroComoCasoLimite()`: Valida o que acontece com 0 iterações.
- `testOuroNaoSeCriaNemSeDestroi()`: Garante a conservação do ouro total na simulação.
- `testGuardiaoComOuroInicialAumentado()`: Verifica o comportamento do guardião com ouro inicial elevado.

**Total**: 12 testes

---

### 3. **Testes Funcionais** (`funcional/`)

**Objetivo**: Validam o funcionamento correto das principais funcionalidades do sistema de ponta a ponta.

**Focam em**:

- Execução completa e correta da simulação.
- Funcionamento do guardião e sua interação com as criaturas.
- Formação e eliminação de clusters.
- Lógica de roubo e transferência de ouro.

**Testes Incluídos**:

- `testFuncionamentoSimulacao()`: Executa uma simulação completa e valida o resultado.
- `testFuncionamentoGuardiao()`: Testa a criação, o comportamento e a eliminação do guardião.
- `testClusterFormacaoEliminacao()`: Valida a lógica de formação e destruição de clusters de criaturas.

**Total**: 9 testes

---

### 4. **Testes Estruturais** (`estrutural/`)

**Objetivo**: Verificam as interações entre os componentes internos do sistema, como classes e métodos.

**Focam em**:

- Interação entre criaturas (cálculo de distância, roubo).
- Validação de parâmetros (nulidade, estado interno).
- Comportamento de métodos específicos de forma isolada.

**Testes Incluídos**:

- `testInteracaoRoubo()`: Testa o mecanismo de roubo entre duas criaturas.
- `testCalculoDistancia()`: Valida se o cálculo de distância euclidiana está correto.
- `testEncontrarMaisProximaComportamento()`: Verifica a lógica para encontrar a criatura mais próxima.
- `testValidacaoParametrosDistancia()`: Garante que o método lida corretamente com parâmetros nulos.

**Total**: 13 testes

---

### 5. **Testes com Dublês** (`duplas/`)

**Objetivo**: Testam componentes de forma isolada, utilizando dublês (mocks) para simular dependências.

**Focam em**:

- Isolar o `SimuladorService` de suas dependências (`SimuladorUtils`).
- Validar a lógica do serviço sem a interferência de outros componentes.
- Garantir que os métodos corretos das dependências são chamados.

**Testes Incluídos**:

- `testProcessarCriaturasComMock()`: Testa o processamento de criaturas usando um mock para `SimuladorUtils`.

**Total**: 1 teste

## 📊 Resumo dos Testes

| Categoria      | Quantidade | Foco Principal                 |
| -------------- | ---------- | ------------------------------ |
| **Domínio**    | 13         | Regras de negócio e restrições |
| **Fronteira**  | 12         | Limites e casos extremos       |
| **Funcional**  | 9          | Funcionamento de ponta a ponta |
| **Estrutural** | 13         | Interações entre componentes   |
| **Dublês**     | 1          | Testes isolados com mocks      |
| **TOTAL**      | **48**     | **Cobertura completa**         |

## 🚀 Como Executar

### Executar todos os testes:

```bash
mvn test
```

### Executar apenas uma categoria:

```bash
# Testes de domínio
mvn test -Dtest="com.example.saltitantes.dominio.*"

# Testes de fronteira
mvn test -Dtest="com.example.saltitantes.fronteira.*"

# Testes funcionais
mvn test -Dtest="com.example.saltitantes.funcional.*"

# Testes estruturais
mvn test -Dtest="com.example.saltitantes.estrutural.*"

# Testes com dublês
mvn test -Dtest="com.example.saltitantes.duplas.*"
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

## 🔄 Migração dos Testes Originais

Todos os **28 testes originais** foram **migrados e expandidos** para **48 testes**, mantendo a funcionalidade original e adicionando novos testes para melhor cobertura:

- ✅ **Funcionalidade preservada**: Todos os testes originais continuam funcionando.
- ✅ **Cobertura expandida**: Novos testes adicionados para casos específicos.
- ✅ **Documentação melhorada**: Cada teste agora tem documentação clara do seu propósito.
- ✅ **Organização aprimorada**: Estrutura lógica por tipo de teste.

---

**🎉 Resultado**: Estrutura de testes mais robusta, organizada e fácil de manter!
