# VZDolci-BackEnd
Back-end para gerência do site da doceria VZ Dolci

## 📋 Descrição
API REST desenvolvida em Java com Spring Boot para gerenciar os produtos da doceria VZ Dolci. O sistema utiliza PostgreSQL (Supabase) como banco de dados e segue os princípios de Clean Architecture.

## 🛠️ Tecnologias
- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- **PostgreSQL** (via Supabase)
- **Flyway** (migrações de banco de dados)
- **Gradle** (gerenciamento de dependências)

## 📁 Estrutura do Projeto
```
src/main/java/com/vzdolci/backend/
├── domain/model/              # Modelos de domínio (puro Java)
│   └── Product.java
├── application/
│   ├── exception/             # Exceções customizadas
│   ├── mapper/                # Conversores entre entidade e domínio
│   └── usecase/               # Casos de uso (lógica de negócio)
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/            # Entidades JPA
│   │   └── repository/        # Repositórios Spring Data
│   └── web/
│       ├── controller/        # Controllers REST
│       └── dto/               # DTOs para API
└── VzDolciBackendApplication.java
```

## 🗄️ Banco de Dados

### Configuração do Supabase
1. Crie um projeto no [Supabase](https://app.supabase.com/)
2. Obtenha as credenciais de conexão em **Project Settings → Database**
3. Configure as variáveis de ambiente (veja seção abaixo)

### Schema do Banco
A tabela `products` é criada automaticamente pelo Flyway na primeira execução:
- `id`: BIGSERIAL (chave primária)
- `name`: VARCHAR(150)
- `description`: TEXT
- `price_cents`: INTEGER (preço em centavos)
- `ingredients`: TEXT
- `story`: TEXT
- `emoji`: VARCHAR(16)
- `slug`: VARCHAR(160) (único)
- `is_active`: BOOLEAN
- `created_at`: TIMESTAMPTZ
- `updated_at`: TIMESTAMPTZ (atualizado automaticamente via trigger)

## ⚙️ Configuração

### Variáveis de Ambiente (Produção)
```bash
SUPABASE_HOST=db.xxxxxx.supabase.co
SUPABASE_PORT=5432
SUPABASE_DB=postgres
SUPABASE_USER=postgres
SUPABASE_PASSWORD=sua_senha_aqui
```

### Variáveis de Ambiente (Desenvolvimento)
```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=vzdolci
DB_USER=postgres
DB_PASSWORD=postgres
```

## 🚀 Como Executar

### Desenvolvimento Local
```bash
# Com banco PostgreSQL local
./gradlew bootRun --args='--spring.profiles.active=dev'

# Ou com Docker Compose (se configurado)
docker-compose up -d
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Produção (Supabase)
```bash
# Definir variáveis de ambiente primeiro
export SUPABASE_HOST=db.xxxxxx.supabase.co
export SUPABASE_PORT=5432
export SUPABASE_DB=postgres
export SUPABASE_USER=postgres
export SUPABASE_PASSWORD=sua_senha

# Executar
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### Build do Projeto
```bash
# Compilar e gerar JAR
./gradlew clean build

# Executar JAR
java -jar build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar
```

## 📡 Endpoints da API

### Base URL
```
http://localhost:8080/api/v1
```

### Produtos

#### Listar todos os produtos
```http
GET /api/v1/products
```

**Parâmetros Query:**
- `activeOnly` (opcional): `true` ou `false` (padrão: `false`)

**Resposta:**
```json
[
  {
    "id": 1,
    "name": "Panna Cotta Clássica",
    "description": "Sobremesa italiana cremosa",
    "price": 25.00,
    "ingredients": "Creme de leite, açúcar, baunilha",
    "story": "Inspirada na tradição piemontesa",
    "emoji": "🍮"
  }
]
```

#### Buscar produto por ID
```http
GET /api/v1/products/{id}
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "name": "Panna Cotta Clássica",
  "description": "Sobremesa italiana cremosa",
  "price": 25.00,
  "ingredients": "Creme de leite, açúcar, baunilha",
  "story": "Inspirada na tradição piemontesa",
  "emoji": "🍮"
}
```

**Resposta (404 Not Found):**
```json
{
  "message": "Product not found with id: 99",
  "status": 404,
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 🧪 Testes
```bash
# Executar testes
./gradlew test

# Relatório de testes
./gradlew test --info
```

## 🔒 Segurança
- **CORS**: Habilitado para todas as origens (ajustar para produção)
- **SSL**: Obrigatório para conexões com Supabase
- **RLS (Row Level Security)**: Opcional no Supabase (recomendado se expor diretamente)

## 📦 Deploy

### Preparar para Deploy
1. Configurar variáveis de ambiente no servidor
2. Build do projeto: `./gradlew clean build`
3. Deploy do JAR gerado em `build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar`

### Plataformas Sugeridas
- **Railway**: Suporte nativo para Spring Boot
- **Render**: Deploy automático via Git
- **Heroku**: Com buildpack Java
- **AWS Elastic Beanstalk**: Para produção escalável

## 🔄 Migrações de Banco de Dados

As migrações são gerenciadas pelo Flyway e estão em:
```
src/main/resources/db/migration/
```

Para adicionar nova migração:
1. Criar arquivo `V{número}__{descrição}.sql`
2. Exemplo: `V2__add_category_table.sql`
3. A migração será aplicada automaticamente no próximo startup

## 📝 Futuras Melhorias
- [ ] Adicionar autenticação e autorização (Spring Security + JWT)
- [ ] Implementar endpoints de criação/atualização/deleção de produtos
- [ ] Adicionar tabela de categorias
- [ ] Integração com Supabase Storage para imagens
- [ ] Implementar cache (Redis)
- [ ] Adicionar paginação nos endpoints de listagem
- [ ] Documentação OpenAPI/Swagger
- [ ] Implementar busca por texto (nome, descrição, ingredientes)

## 👥 Contribuidores
- Desenvolvido para VZ Dolci

## 📄 Licença
Este projeto está sob a licença GNU General Public License v3.0 - veja o arquivo [LICENSE](LICENSE) para detalhes.

