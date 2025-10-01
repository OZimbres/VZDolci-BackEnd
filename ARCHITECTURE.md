# Arquitetura do Sistema - VZDolci Backend

## 📐 Visão Geral

O VZDolci Backend segue os princípios de **Clean Architecture**, garantindo separação de responsabilidades, testabilidade e independência de frameworks.

## 🏗️ Camadas da Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                       INTERFACE LAYER                        │
│                    (infrastructure/web)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ Controllers  │  │     DTOs     │  │ Exception Handler│  │
│  │   REST API   │  │   Request/   │  │  Global Error    │  │
│  │   Endpoints  │  │   Response   │  │    Handling      │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                      APPLICATION LAYER                       │
│                      (application)                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  Use Cases   │  │   Mappers    │  │   Exceptions     │  │
│  │   Business   │  │   Entity ↔   │  │   Custom Error   │  │
│  │    Logic     │  │    Domain    │  │     Types        │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                        DOMAIN LAYER                          │
│                       (domain/model)                         │
│  ┌─────────────────────────────────────────────────────┐    │
│  │               Product (Domain Model)                │    │
│  │         Pure Java - No Dependencies                 │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                      │
│                   (infrastructure/persistence)               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │   Entities   │  │ Repositories │  │     Flyway       │  │
│  │   JPA (@)    │  │  Spring Data │  │   Migrations     │  │
│  │   Mappings   │  │     JPA      │  │   Database DDL   │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↕
                  ┌──────────────────┐
                  │   PostgreSQL     │
                  │    (Supabase)    │
                  └──────────────────┘
```

## 📂 Estrutura de Diretórios

```
src/main/java/com/vzdolci/backend/
│
├── 📦 domain/                          # DOMAIN LAYER
│   └── model/                          # Entidades de domínio puras
│       └── Product.java                # Modelo de produto (sem anotações)
│
├── 📦 application/                     # APPLICATION LAYER
│   ├── exception/                      # Exceções customizadas
│   │   └── NotFoundException.java      # Exceção 404
│   ├── mapper/                         # Conversores Entity ↔ Domain
│   │   └── ProductMapper.java          # Mapper de produtos
│   └── usecase/                        # Casos de uso (lógica de negócio)
│       ├── GetAllProductsUseCase.java  # Listar produtos
│       └── GetProductByIdUseCase.java  # Buscar por ID
│
├── 📦 infrastructure/                  # INFRASTRUCTURE LAYER
│   ├── persistence/                    # Camada de persistência
│   │   ├── entity/                     # Entidades JPA
│   │   │   └── ProductEntity.java      # Entity mapeada para DB
│   │   └── repository/                 # Repositórios Spring Data
│   │       └── ProductRepository.java  # Interface JPA
│   └── web/                            # Camada web
│       ├── controller/                 # Controllers REST
│       │   ├── ProductController.java  # Endpoints de produtos
│       │   └── GlobalExceptionHandler.java # Tratamento global
│       └── dto/                        # Data Transfer Objects
│           ├── ProductResponse.java    # DTO de resposta
│           └── ErrorResponse.java      # DTO de erro
│
└── VzDolciBackendApplication.java     # Classe principal Spring Boot
```

## 🔄 Fluxo de Dados

### Requisição GET /api/v1/products/1

```
1. HTTP Request
   │
   └─→ ProductController.getProductById(1)
         │
         └─→ GetProductByIdUseCase.execute(1)
               │
               ├─→ ProductRepository.findById(1)
               │     │
               │     └─→ PostgreSQL Query
               │           │
               │           └─→ ProductEntity (JPA)
               │
               └─→ ProductMapper.toDomain(entity)
                     │
                     └─→ Product (Domain Model)
                           │
                           └─→ ProductResponse.fromDomain(product)
                                 │
                                 └─→ HTTP Response (JSON)
```

### Sequência Detalhada

```sequence
Cliente->Controller: GET /api/v1/products/1
Controller->UseCase: execute(1)
UseCase->Repository: findById(1)
Repository->Database: SELECT * FROM products WHERE id=1
Database-->Repository: ProductEntity
Repository-->UseCase: ProductEntity
UseCase->Mapper: toDomain(entity)
Mapper-->UseCase: Product
UseCase-->Controller: Product
Controller->DTO: fromDomain(product)
DTO-->Controller: ProductResponse
Controller-->Cliente: 200 OK + JSON
```

## 🎯 Princípios Aplicados

### 1. Separation of Concerns
Cada camada tem uma responsabilidade específica:
- **Domain**: Regras de negócio puras
- **Application**: Orquestração de casos de uso
- **Infrastructure**: Detalhes técnicos (DB, Web)

### 2. Dependency Inversion
```
Infrastructure → Application → Domain
(depende de)     (depende de)   (não depende de nada)
```

### 3. Single Responsibility
Cada classe tem uma única responsabilidade:
- `ProductController`: Apenas receber requisições HTTP
- `GetProductByIdUseCase`: Apenas buscar produto por ID
- `ProductMapper`: Apenas converter Entity ↔ Domain

### 4. Open/Closed Principle
Extensível sem modificar código existente:
- Adicionar novos endpoints → Novos controllers
- Adicionar novos casos de uso → Novas classes UseCase

## 🗄️ Modelo de Dados

### Tabela `products`

```sql
┌─────────────┬─────────────────┬──────────────────────────────────────┐
│   Coluna    │      Tipo       │            Descrição                 │
├─────────────┼─────────────────┼──────────────────────────────────────┤
│ id          │ BIGSERIAL       │ Chave primária                       │
│ name        │ VARCHAR(150)    │ Nome do produto                      │
│ description │ TEXT            │ Descrição detalhada                  │
│ price_cents │ INTEGER         │ Preço em centavos (2500 = R$ 25,00) │
│ ingredients │ TEXT            │ Lista de ingredientes                │
│ story       │ TEXT            │ História do produto                  │
│ emoji       │ VARCHAR(16)     │ Emoji representativo                 │
│ slug        │ VARCHAR(160)    │ URL-friendly (único)                 │
│ is_active   │ BOOLEAN         │ Produto ativo/inativo                │
│ created_at  │ TIMESTAMPTZ     │ Data de criação                      │
│ updated_at  │ TIMESTAMPTZ     │ Data de atualização (auto)           │
└─────────────┴─────────────────┴──────────────────────────────────────┘

Índices:
  - PRIMARY KEY: id
  - UNIQUE: slug
  - INDEX: is_active
  - INDEX: slug

Triggers:
  - trg_products_updated_at: Atualiza updated_at automaticamente
```

### Conversão de Preços

```java
// Database: INTEGER (price_cents)
price_cents = 2500

// Mapper: ProductMapper.toDomain()
BigDecimal price = BigDecimal.valueOf(2500).movePointLeft(2)
// Result: 25.00

// API Response: BigDecimal
{
  "price": 25.00
}
```

## 🔌 Integrações

### Spring Boot Components

```
┌─────────────────────────────────────────────┐
│        Spring Boot Application              │
│  ┌───────────────────────────────────────┐  │
│  │    Spring Web (REST API)              │  │
│  │    - @RestController                  │  │
│  │    - @RequestMapping                  │  │
│  └───────────────────────────────────────┘  │
│  ┌───────────────────────────────────────┐  │
│  │    Spring Data JPA                    │  │
│  │    - @Repository                      │  │
│  │    - JpaRepository<T, ID>             │  │
│  └───────────────────────────────────────┘  │
│  ┌───────────────────────────────────────┐  │
│  │    Flyway                             │  │
│  │    - Database Migrations              │  │
│  │    - V1__create_products_table.sql    │  │
│  └───────────────────────────────────────┘  │
│  ┌───────────────────────────────────────┐  │
│  │    Hibernate (JPA Implementation)     │  │
│  │    - EntityManager                    │  │
│  │    - ORM Mappings                     │  │
│  └───────────────────────────────────────┘  │
│  ┌───────────────────────────────────────┐  │
│  │    HikariCP (Connection Pool)         │  │
│  │    - max-pool-size: 5                 │  │
│  │    - connection-timeout: 30s          │  │
│  └───────────────────────────────────────┘  │
└─────────────────────────────────────────────┘
                    ↕
            ┌─────────────┐
            │ PostgreSQL  │
            │  (Supabase) │
            └─────────────┘
```

## 🚀 Deploy Architecture

```
┌──────────────┐
│   Frontend   │ (Futuro)
│  React/Vue   │
└──────┬───────┘
       │ HTTPS
       ↓
┌──────────────────────────────────────┐
│    Reverse Proxy / Load Balancer     │
│    (Railway / Render / Heroku)       │
└──────────────┬───────────────────────┘
               │
               ↓
┌──────────────────────────────────────┐
│       VZDolci Backend Instance       │
│       (Spring Boot Application)      │
│    ┌────────────────────────────┐    │
│    │   Tomcat Embedded Server   │    │
│    │   Port: 8080               │    │
│    └────────────────────────────┘    │
└──────────────┬───────────────────────┘
               │ SSL/TLS
               ↓
┌──────────────────────────────────────┐
│         Supabase PostgreSQL          │
│    db.xxxxxx.supabase.co:5432        │
│    ┌────────────────────────────┐    │
│    │  Connection Pooling        │    │
│    │  (pgbouncer)               │    │
│    └────────────────────────────┘    │
└──────────────────────────────────────┘
```

## 🔐 Segurança

### Camadas de Segurança

1. **Transporte**: HTTPS/TLS obrigatório
2. **Database**: SSL mode required para conexões
3. **Connection Pool**: Limita conexões simultâneas
4. **Error Handling**: Não expõe stack traces em produção
5. **CORS**: Configurável por ambiente

### Fluxo de Segurança

```
Request → HTTPS → CORS Check → Controller → UseCase → Repository → SSL → Database
```

## 📊 Performance

### Otimizações Implementadas

1. **Connection Pooling (HikariCP)**
   - Reutiliza conexões database
   - Reduz overhead de conexão

2. **Índices de Database**
   - `idx_products_is_active`: Filtragem rápida
   - `idx_products_slug`: Busca por slug

3. **JPA Lazy Loading**
   - `open-in-view: false`: Evita N+1 queries

4. **Flyway**
   - Migrations versionadas
   - Rollback controlado

## 🧪 Testabilidade

### Pontos de Teste

```
┌─────────────────────────────────────┐
│   Unit Tests                        │
│   - UseCases (isolated)             │
│   - Mappers                         │
│   - Domain Models                   │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│   Integration Tests                 │
│   - Controllers + UseCases          │
│   - Repository + Database (H2)      │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│   E2E Tests                         │
│   - Full application context        │
│   - Real HTTP requests              │
└─────────────────────────────────────┘
```

## 🔮 Futuras Expansões

### Planejamento de Novas Funcionalidades

1. **Autenticação/Autorização**
   ```
   infrastructure/security/
   ├── JwtTokenProvider.java
   ├── SecurityConfig.java
   └── UserDetailsService.java
   ```

2. **Cache Layer**
   ```
   application/cache/
   └── ProductCacheService.java (Redis)
   ```

3. **Eventos de Domínio**
   ```
   domain/event/
   ├── ProductCreatedEvent.java
   └── ProductUpdatedEvent.java
   ```

4. **Paginação**
   ```
   infrastructure/web/dto/
   ├── PageRequest.java
   └── PageResponse.java
   ```

---

Esta arquitetura foi projetada para ser:
- ✅ **Manutenível**: Fácil de entender e modificar
- ✅ **Testável**: Camadas independentes
- ✅ **Escalável**: Pronta para crescer
- ✅ **Resiliente**: Tratamento de erros robusto
