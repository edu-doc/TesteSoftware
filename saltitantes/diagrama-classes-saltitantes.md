# Diagrama de Classes - Sistema Saltitantes

Este diagrama representa a arquitetura da aplicação Saltitantes, focando nas funcionalidades de simulação e usuário.

## Descrição do Sistema

O sistema **Saltitantes** é uma aplicação Spring Boot que simula criaturas que se movem em um horizonte, formam clusters e interagem com um guardião. Os usuários podem executar simulações e acompanhar estatísticas.

## Arquitetura

- **Controllers**: Camada de apresentação (API REST)
- **Services**: Camada de negócio com lógica da aplicação
- **Entities**: Camada de modelo/domínio
- **Repository**: Camada de persistência
- **DTOs**: Objetos de transferência de dados

```mermaid
classDiagram
    %% ==================== CONTROLLERS ====================
    class SimuladorController {
        -SimuladorService simuladorService
        +simular(ParametrosDTO): ResponseEntity<?>
    }

    class UsuarioController {
        -UsuarioService usuarioService
        +criarUsuario(CriarUsuarioDTO): ResponseEntity<UsuarioDTO>
        +login(LoginDTO): ResponseEntity<LoginResponse>
        +listarUsuarios(): ResponseEntity<List<UsuarioDTO>>
        +excluirUsuario(String): ResponseEntity<?>
        +obterEstatisticas(): ResponseEntity<EstatisticasDTO>
    }

    %% ==================== SERVICES ====================
    class SimuladorService {
        -UsuarioService usuarioService
        -List<Criaturas> criaturas
        -List<Cluster> clusters
        -Guardiao guardiao
        -List<SimularResponseDTO> historicoSimulacoes
        +inicializar(int): void
        +simular(int): List<SimularResponseDTO>
        +simular(int, String): List<SimularResponseDTO>
        -processarCriaturas(): Map<Integer, Integer>
        -processarClusters(): Map<Integer, Integer>
        -processarGuardiao(): int
        -verificarGanhador(): boolean
        +encontrarMaisProxima(Criaturas): Criaturas
        +distancia(Criaturas, Criaturas): double
        +eliminarCriaturasPoucoOuro(List<Criaturas>): void
    }

    class UsuarioService {
        -UserRepository userRepository
        +criarUsuario(CriarUsuarioDTO): UsuarioDTO
        +login(LoginDTO): UsuarioDTO
        +listarUsuarios(): List<UsuarioDTO>
        +excluirUsuario(String): void
        +registrarSimulacao(String, boolean): void
        +obterEstatisticas(): EstatisticasDTO
        +obterEstatisticasUsuario(String): UsuarioDTO
        -converterParaDTO(Usuario): UsuarioDTO
        +usuarioExiste(String): boolean
    }

    %% ==================== ENTITIES ====================
    class Usuario {
        -Long id
        -String login
        -String senha
        -String avatar
        -int pontuacao
        -int totalSimulacoes
        +Usuario(String, String, String)
        +incrementarPontuacao(): void
        +incrementarTotalSimulacoes(): void
        +getTaxaSucesso(): double
        +verificarSenha(String): boolean
    }

    class Criaturas {
        -int id
        -int ouro
        -double posicaox
        -static int contador
        +Criaturas()
        +moverX(): void
        +adicionarOuro(int): void
        +perderOuro(int): void
        +count(): int
        +resetarContador(): void
    }

    class Cluster {
        -List<Integer> idscriaturas
        -int ouroTotal
        -double posicaox
        -int idCluster
        +Cluster(Criaturas, Criaturas)
        +adicionarCriatura(Criaturas): void
        +moverX(): double
        +roubarOuro(int): void
        +estaNaPosicao(double): boolean
        +getTamanho(): int
        +contemCriatura(int): boolean
    }

    class Guardiao {
        -int id
        -int ouro
        -double posicaox
        +Guardiao(int)
        +moverX(): void
        +adicionarOuro(int): void
        +estaNaPosicao(double): boolean
    }

    %% ==================== REPOSITORY ====================
    class UserRepository {
        <<interface>>
        +findByLogin(String): Optional<Usuario>
        +save(Usuario): Usuario
        +findAll(): List<Usuario>
        +delete(Usuario): void
    }

    %% ==================== DTOs ====================
    class ParametrosDTO {
        -int quantidade
        -int iteracoes
        -String loginUsuario
    }

    class SimularResponseDTO {
        -int iteracao
        -CriaturasDTO[] criaturas
        -List<ClusterDTO> clusters
        -GuardiaoDTO guardiao
        -boolean simulacaoBemSucedida
    }

    class UsuarioDTO {
        -String login
        -String avatar
        -int pontuacao
        -int totalSimulacoes
        -double taxaSucesso
    }

    class CriarUsuarioDTO {
        -String login
        -String senha
        -String avatar
    }

    class LoginDTO {
        -String login
        -String senha
    }

    class EstatisticasDTO {
        -List<UsuarioDTO> usuarios
        -int totalSimulacoes
        -double mediaSimulacoesSucessoUsuario
        -double mediaTotalSimulacoesSucesso
        -int totalUsuarios
    }

    class CriaturasDTO {
        -int id
        -int ouro
        -double posicaox
        -int criaturaSendoRoubada
    }

    class ClusterDTO {
        -int idCluster
        -List<Integer> idscriaturas
        -int ouroTotal
        -double posicaox
        -int criaturaSendoRoubada
    }

    class GuardiaoDTO {
        -int id
        -int ouro
        -double posicaox
        -int clusterEliminado
    }

    %% ==================== RELACIONAMENTOS ====================

    %% Controllers -> Services
    SimuladorController --> SimuladorService : usa
    UsuarioController --> UsuarioService : usa

    %% Services -> Repositories
    UsuarioService --> UserRepository : usa

    %% Services -> Entities
    SimuladorService --> Criaturas : gerencia lista
    SimuladorService --> Cluster : gerencia lista
    SimuladorService --> Guardiao : possui
    SimuladorService --> UsuarioService : usa
    UsuarioService --> Usuario : gerencia

    %% Entities relationships
    Cluster --> Criaturas : composto por múltiplas
    UserRepository --> Usuario : persiste

    %% DTOs relationships
    SimuladorController --> ParametrosDTO : recebe
    SimuladorController --> SimularResponseDTO : retorna
    SimularResponseDTO --> CriaturasDTO : contém array
    SimularResponseDTO --> ClusterDTO : contém lista
    SimularResponseDTO --> GuardiaoDTO : contém

    UsuarioController --> CriarUsuarioDTO : recebe
    UsuarioController --> LoginDTO : recebe
    UsuarioController --> UsuarioDTO : retorna
    UsuarioController --> EstatisticasDTO : retorna

    %% Service -> DTO conversions
    SimuladorService --> SimularResponseDTO : cria
    UsuarioService --> UsuarioDTO : converte de Usuario
    UsuarioService --> EstatisticasDTO : cria

    %% Styling
    classDef controllerClass fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef serviceClass fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef entityClass fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef dtoClass fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef repositoryClass fill:#fce4ec,stroke:#880e4f,stroke-width:2px

    class SimuladorController:::controllerClass
    class UsuarioController:::controllerClass
    class SimuladorService:::serviceClass
    class UsuarioService:::serviceClass
    class Usuario:::entityClass
    class Criaturas:::entityClass
    class Cluster:::entityClass
    class Guardiao:::entityClass
    class ParametrosDTO:::dtoClass
    class SimularResponseDTO:::dtoClass
    class UsuarioDTO:::dtoClass
    class CriarUsuarioDTO:::dtoClass
    class LoginDTO:::dtoClass
    class EstatisticasDTO:::dtoClass
    class CriaturasDTO:::dtoClass
    class ClusterDTO:::dtoClass
    class GuardiaoDTO:::dtoClass
    class UserRepository:::repositoryClass
```

## Funcionalidades Principais

### Simulação

- **Inicialização**: Cria criaturas e guardião
- **Movimento**: Criaturas se movem no horizonte baseado em seu ouro
- **Formação de Clusters**: Criaturas próximas se agrupam
- **Eliminação**: Guardião elimina clusters próximos
- **Condições de Vitória**: Simulação termina quando resta apenas guardião ou guardião + 1 criatura

### Gestão de Usuários

- **Cadastro e Login**: Sistema de autenticação simples
- **Estatísticas**: Acompanhamento de simulações bem-sucedidas
- **Ranking**: Taxa de sucesso por usuário

## Patterns Utilizados

- **MVC**: Separação clara entre Controller, Service e Model
- **DTO Pattern**: Objetos para transferência de dados
- **Repository Pattern**: Abstração da camada de persistência
- **Dependency Injection**: Através do Spring Framework
