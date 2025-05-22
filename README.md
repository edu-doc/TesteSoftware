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

- JUnit 5 (com testes parametrizados)
- AssertJ

### Estrutura dos Testes

Os testes automatizados estão localizados em:

```
src/test/java/com/example/saltitantes/SaltitantesApplicationTests.java
```

### Tipos e Detalhamento dos Testes

Abaixo estão descritos detalhadamente todos os testes implementados, agrupados por tipo e com explicação do cenário e objetivo de cada um:

---

#### 1. Testes de Fronteira e Domínio

##### testInicializacao (ParameterizedTest, com inicializacaoProvider)
- **Tipo:** Fronteira e domínio
- **Cenário:** Inicialização do simulador com diferentes valores de criaturas
- **O que valida:**
  - Se o número de criaturas for menor que 2 ou maior que 1000, deve lançar exceção (validação de limites).
  - Se o número estiver dentro do permitido, o simulador inicializa corretamente e o histórico de simulações começa vazio.
- **Motivação:** Garante que o sistema respeita as restrições de negócio para o número de criaturas.
- **Exemplos testados:**
  - n = -1 (inválido, lança exceção)
  - n = 1 (inválido, lança exceção)
  - n = 1000 (válido)
  - n = 1001 (inválido, lança exceção)

---

#### 2. Testes Funcionais de Simulação

##### testSimulacao (ParameterizedTest, com simulacaoProvider)
- **Tipo:** Funcional
- **Cenário:** Simulação de diferentes quantidades de criaturas e iterações
- **O que valida:**
  - O método simular retorna uma lista com o número correto de iterações
  - Em cada iteração, a quantidade de criaturas está correta
- **Motivação:** Garante que a simulação executa corretamente para diferentes cenários
- **Exemplos testados:**
  - 2 criaturas, 1 iteração
  - 3 criaturas, 3 iterações
  - 5 criaturas, 5 iterações

##### testSimulacaoComZeroIteracoes (ParameterizedTest, com iteracoesZeroProvider)
- **Tipo:** Fronteira/Funcional
- **Cenário:** Simulação com zero iterações
- **O que valida:**
  - O método simular retorna lista vazia quando o número de iterações é zero
- **Motivação:** Garante que o sistema lida corretamente com o caso limite de não executar nenhuma simulação

---

#### 3. Testes Estruturais e de Interação

##### testRoubos (ParameterizedTest, com rouboProvider)
- **Tipo:** Estrutural/Interação
- **Cenário:** Simulação em que é esperado que ocorra pelo menos um roubo
- **O que valida:**
  - Após a simulação, pelo menos uma criatura deve ter realizado um roubo (campo idCriaturaRoubada diferente de -1)
- **Motivação:** Garante que o mecanismo de roubo está funcionando e que a interação entre criaturas ocorre

##### testVizinhaNaoPodeSerRoubadaSeNaoTemOuro (ParameterizedTest, com vizinhaSemOuroProvider)
- **Tipo:** Estrutural/Negócio
- **Cenário:** Duas criaturas vizinhas, mas apenas uma tem ouro
- **O que valida:**
  - A criatura sem ouro não pode ser roubada (idCriaturaRoubada permanece -1)
- **Motivação:** Garante a integridade da regra de negócio: só é possível roubar quem tem ouro

##### testVizinhaPodeSerRoubadaSeTemOuro (ParameterizedTest, com vizinhaComOuroProvider)
- **Tipo:** Estrutural/Negócio
- **Cenário:** Duas criaturas vizinhas, ambas com ouro
- **O que valida:**
  - O roubo ocorre e o campo idCriaturaRoubada indica que houve transferência de ouro
- **Motivação:** Verifica que a lógica de roubo é aplicada corretamente quando as condições são atendidas

---

### Resumo dos Tipos de Teste

- **Fronteira:** Validam limites de entrada e comportamento em extremos (ex: inicialização, zero iterações)
- **Funcional:** Validam o funcionamento correto dos métodos principais do sistema (ex: simulação)
- **Estrutural/Interação:** Verificam as interações entre entidades e regras de negócio específicas (ex: roubo entre criaturas)

Todos os testes utilizam parametrização para cobrir múltiplos cenários automaticamente, aumentando a robustez e a cobertura do sistema.

  - As interações ocorram conforme o esperado.

#### 🔹 Teste de Movimento

- Verifica:
  - Se criaturas se movem corretamente dentro dos limites do mundo.
  - Que não há movimentações para fora do espaço permitido.

#### 🔹 Teste de Combate e Roubo

- Simula o encontro entre duas criaturas.
- Garante que o ouro seja transferido de forma correta.
- Valida que o perdedor não fique com valores negativos.

## 📝 Critérios de Qualidade e Validação

- ✔️ Nenhuma exceção não tratada durante a simulação.
- ✔️ Consistência do total de ouro.
- ✔️ Garantia de movimentação válida.
- ✔️ Testes unitários cobrindo cenários diversos.
