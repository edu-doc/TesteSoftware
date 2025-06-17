# 📁 Estrutura Organizada de Testes

Este projeto agora possui uma estrutura de testes organizada por **tipo de teste**, facilitando a manutenção, compreensão e execução dos testes.

## 📂 Estrutura de Pastas

```
src/test/java/com/example/saltitantes/
├── dominio/               # Testes de Domínio
│   └── TesteDominio.java
├── fronteira/             # Testes de Fronteira  
│   └── TesteFronteira.java
├── funcional/             # Testes Funcionais
│   └── TesteFuncional.java
├── estrutural/            # Testes Estruturais
│   └── TesteEstrutural.java
└── SaltitantesApplicationTests_BACKUP.java  # Backup do arquivo original
```

## 🎯 Tipos de Teste

### 1. **Testes de Domínio** (`dominio/`)
**Objetivo**: Validam regras de negócio e restrições essenciais do sistema.

**Focam em**:
- Limites mínimos e máximos de entrada
- Condições de operação válidas  
- Restrições de negócio fundamentais

**Testes Incluídos**:
- `testInicializacaoRegrasNegocio()` - Valida regras para número de criaturas (2-1000)
- `testIteracoesRegrasNegocio()` - Valida regras para número de iterações (1-1000)

**Total**: 13 testes

---

### 2. **Testes de Fronteira** (`fronteira/`)
**Objetivo**: Validam limites de entrada e comportamento em extremos.

**Focam em**:
- Casos limite (valores mínimos e máximos)
- Situações extremas do sistema
- Comportamento em bordas dos intervalos válidos

**Testes Incluídos**:
- `testSemVizinhaNaoRoubada()` - Caso extremo com apenas uma criatura
- `testIteracaoZeroComoCasoLimite()` - Validação de zero iterações
- `testValoresExatosDeFronteira()` - Valores exatos nos limites (1, 2, 1000, 1001)
- `testIteracoesCasosLimite()` - Casos limite para iterações

**Total**: 12 testes

---

### 3. **Testes Funcionais** (`funcional/`)
**Objetivo**: Validam o funcionamento correto dos métodos principais do sistema.

**Focam em**:
- Execução correta da simulação
- Retorno adequado de resultados
- Funcionamento dos algoritmos principais
- Integridade dos dados durante o processamento

**Testes Incluídos**:
- `testFuncionamentoSimulacao()` - Execução correta da simulação
- `testFuncionamentoInicializacao()` - Funcionamento da inicialização
- `testFuncionamentoGuardiao()` - Criação e configuração do guardião

**Total**: 9 testes

---

### 4. **Testes Estruturais** (`estrutural/`)
**Objetivo**: Verificam as interações entre entidades e regras de negócio específicas.

**Focam em**:
- Interações entre criaturas (roubo)
- Validação de parâmetros e estado interno
- Comportamento de métodos específicos
- Integridade das relações entre objetos

**Testes Incluídos**:
- `testInteracaoRoubo()` - Mecanismo de roubo entre criaturas
- `testVizinhaSemOuroNaoPodeSerRoubada()` - Regra: sem ouro = sem roubo
- `testVizinhaComOuroPodeSerRoubada()` - Regra: com ouro = pode ser roubada
- `testValidacaoParametrosDistancia()` - Validação de parâmetros nulos
- `testValidacaoParametrosEncontrarMaisProxima()` - Validação de referências nulas
- `testCalculoDistancia()` - Funcionamento do cálculo de distância
- `testEncontrarMaisProximaComportamento()` - Lógica de busca da criatura mais próxima

**Total**: 13 testes

## 📊 Resumo dos Testes

| Categoria | Quantidade | Foco Principal |
|-----------|------------|----------------|
| **Domínio** | 13 | Regras de negócio e restrições |
| **Fronteira** | 12 | Limites e casos extremos |
| **Funcional** | 9 | Funcionamento dos algoritmos |
| **Estrutural** | 13 | Interações e validações |
| **TOTAL** | **47** | **Cobertura completa** |

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
```

### Executar um teste específico:
```bash
mvn test -Dtest="TesteDominio#testInicializacaoRegrasNegocio"
```

## ✅ Benefícios da Nova Estrutura

1. **📁 Organização Clara**: Cada tipo de teste em sua pasta específica
2. **🎯 Propósito Definido**: Cada categoria tem objetivos claros e bem documentados
3. **🔍 Fácil Manutenção**: Localização rápida de testes por funcionalidade
4. **📚 Melhor Documentação**: Cada teste possui documentação detalhada do seu propósito
5. **🧪 Execução Seletiva**: Possibilidade de executar apenas categorias específicas
6. **📈 Cobertura Organizada**: Visão clara de que aspectos estão sendo testados

## 🔄 Migração dos Testes Originais

Todos os **28 testes originais** foram **migrados e expandidos** para **47 testes**, mantendo a funcionalidade original e adicionando novos testes para melhor cobertura:

- ✅ **Funcionalidade preservada**: Todos os testes originais continuam funcionando
- ✅ **Cobertura expandida**: Novos testes adicionados para casos específicos  
- ✅ **Documentação melhorada**: Cada teste agora tem documentação clara do seu propósito
- ✅ **Organização aprimorada**: Estrutura lógica por tipo de teste

---

**🎉 Resultado**: Estrutura de testes mais robusta, organizada e fácil de manter!
