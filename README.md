
# 📄 Documentação do Projeto Saltitantes

## 🏆 Descrição

**Saltitantes** é um projeto Java que simula um mundo habitado por criaturas que se movem, interagem e roubam ouro umas das outras. O objetivo do projeto é estudar lógica de programação, modelagem orientada a objetos e garantir a qualidade do software por meio de testes automatizados.

## 🚀 Tecnologias Utilizadas

- Java 17+
- Maven
- JUnit 5 (Jupiter)
- AssertJ
- Spring Boot (opcional, dependendo da configuração do projeto)

## 📦 Estrutura do Projeto

```
src/
 └── main/
      └── java/
           └── com.example.saltitantes/
                ├── model/
                │    ├── Creature.java
                │    ├── World.java
                │    └── service/
                │         ├── SimuladorService.java
                │         └── MovimentoService.java
                └── SaltitantesApplication.java
 └── test/
      └── java/
           └── com.example.saltitantes/
                └── SaltitantesApplicationTests.java
```

## 🛠️ Principais Classes

### ✅ `Creature`

- Representa uma criatura no mundo.
- Atributos:
  - Nome
  - Quantidade de ouro
  - Posição (x, y)
- Métodos:
  - `mover()`: movimenta a criatura aleatoriamente.
  - `roubar()`: interage com outra criatura para roubar ouro.

### ✅ `World`

- Representa o mundo onde as criaturas habitam.
- Atributos:
  - Dimensões do mundo (largura, altura)
  - Lista de criaturas
- Métodos:
  - `adicionarCriatura()`: adiciona uma nova criatura ao mundo.
  - `simular()`: executa uma simulação, movimentando criaturas e resolvendo interações.

### ✅ `SimuladorService`

- Contém a lógica principal de simulação do mundo.
- Responsável por coordenar os movimentos das criaturas e as interações de roubo.
- Métodos:
  - `executarSimulacao()`: executa a simulação completa.
  - `validarConsistencia()`: verifica se o total de ouro no mundo está consistente após a simulação.

### ✅ `MovimentoService`

- Responsável por determinar os movimentos válidos das criaturas, respeitando os limites do mundo.
- Métodos:
  - `moverParaDirecao()`: executa o movimento da criatura em uma direção específica.
  - `validarLimites()`: assegura que a criatura não ultrapasse os limites do mundo.

## 🧪 Testes Automatizados

### ✅ Tecnologias de Teste

- **JUnit 5** com **parâmetros** para testar múltiplas combinações de dados.
- **AssertJ** para uma API fluente de asserções.

### ✅ Estrutura dos Testes

Arquivo principal:  
`src/test/java/com.example.saltitantes/SaltitantesApplicationTests.java`

### ✅ Casos de Teste Implementados

#### 🔹 Teste de Simulação

- Garante que:
  - O total de ouro permaneça inalterado após a simulação.
  - O número de criaturas seja o mesmo antes e depois.
  - As interações ocorram conforme o esperado.

#### 🔹 Teste de Movimento

- Verifica:
  - Se criaturas se movem corretamente dentro dos limites do mundo.
  - Que não há movimentações para fora do espaço permitido.

#### 🔹 Teste de Combate e Roubo

- Simula o encontro entre duas criaturas.
- Garante que o ouro seja transferido de forma correta.
- Valida que o perdedor não fique com valores negativos.

## ⚙️ Como Executar o Projeto

### ✅ Pré-requisitos

- Java JDK 17+
- Maven instalado

### ✅ Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/seuusuario/saltitantes.git
   cd saltitantes
   ```

2. Compile o projeto:
   ```bash
   mvn clean install
   ```

3. Execute os testes:
   ```bash
   mvn test
   ```

4. (Opcional) Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

## 📝 Critérios de Qualidade e Validação

- ✔️ Nenhuma exceção não tratada durante a simulação.
- ✔️ Consistência do total de ouro.
- ✔️ Garantia de movimentação válida.
- ✔️ Testes unitários cobrindo cenários diversos.

## 📌 Padrões de Código

- Utilização de **POO**: encapsulamento, herança e polimorfismo.
- **SOLID**: especialmente o princípio da responsabilidade única.
- **Test Driven Development (TDD)**: testes como base para o desenvolvimento das funcionalidades.

## 👥 Contribuidores

- [Seu Nome](https://github.com/seuusuario) - Desenvolvedor principal

## 📝 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

## ❓ Dúvidas ou Problemas?

Entre em contato através de [seu.email@exemplo.com](mailto:seu.email@exemplo.com) ou abra uma issue neste repositório.

**🚀 Projeto Saltitantes — onde criaturas travam batalhas épicas por ouro!**
