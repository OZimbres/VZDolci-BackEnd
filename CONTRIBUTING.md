# Guia de Contribuição

Obrigado por considerar contribuir para o VZDolci Backend! 🎉

## 🚀 Como Começar

### Pré-requisitos
- Java 17 ou superior
- Docker (opcional, para PostgreSQL local)
- Git

### Configuração do Ambiente

1. **Clone o repositório**
```bash
git clone https://github.com/OZimbres/VZDolci-BackEnd.git
cd VZDolci-BackEnd
```

2. **Configure o banco de dados**

Opção A - Docker (Recomendado):
```bash
docker-compose up -d
```

Opção B - PostgreSQL Local:
- Instale PostgreSQL
- Crie um banco chamado `vzdolci`
- Configure as variáveis de ambiente (veja `.env.example`)

3. **Execute a aplicação**
```bash
./run-dev.sh
```

## 📝 Padrões de Código

### Arquitetura
Seguimos os princípios de **Clean Architecture**:

- **Domain Layer**: Modelos puros, sem dependências de frameworks
- **Application Layer**: Casos de uso e lógica de negócio
- **Infrastructure Layer**: Implementações concretas (controllers, repositories, etc.)

### Estrutura de Packages
```
com.vzdolci.backend/
├── domain/model/           # Entidades de negócio
├── application/
│   ├── usecase/           # Casos de uso
│   ├── mapper/            # Conversores
│   └── exception/         # Exceções customizadas
└── infrastructure/
    ├── persistence/       # Repositórios e entidades JPA
    └── web/              # Controllers e DTOs
```

### Convenções de Nomenclatura

#### Classes
- **Use Cases**: `{Verbo}{Entidade}UseCase` (ex: `GetProductByIdUseCase`)
- **Controllers**: `{Entidade}Controller` (ex: `ProductController`)
- **DTOs**: `{Entidade}Request/Response` (ex: `ProductResponse`)
- **Entities**: `{Entidade}Entity` (ex: `ProductEntity`)
- **Repositories**: `{Entidade}Repository` (ex: `ProductRepository`)

#### Métodos
- **Use Cases**: `execute()` ou `execute({parametros})`
- **Controllers**: Verbos HTTP (ex: `getAllProducts()`, `getProductById()`)

## 🔄 Fluxo de Contribuição

### 1. Crie uma Branch
```bash
git checkout -b feature/minha-funcionalidade
# ou
git checkout -b fix/meu-bugfix
```

### 2. Faça suas Alterações
- Siga os padrões de código
- Escreva código limpo e legível
- Adicione comentários quando necessário
- Mantenha métodos pequenos e focados

### 3. Teste suas Alterações
```bash
./gradlew test
```

### 4. Commit
Use mensagens descritivas em português:
```bash
git commit -m "Adiciona endpoint para criação de produtos"
```

### 5. Push e Pull Request
```bash
git push origin feature/minha-funcionalidade
```

Crie um Pull Request com:
- Descrição clara das mudanças
- Referência a issues relacionadas
- Screenshots (se aplicável)

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
./gradlew test

# Teste específico
./gradlew test --tests com.vzdolci.backend.application.usecase.GetProductByIdUseCaseTest
```

### Escrever Testes
- Use JUnit 5
- Nomeie testes descritivamente: `deveFazer{Acao}Quando{Condicao}()`
- Organize com Given-When-Then

Exemplo:
```java
@Test
void deveBuscarProdutoPorIdQuandoExistir() {
    // Given
    Long id = 1L;
    
    // When
    Product result = useCase.execute(id);
    
    // Then
    assertNotNull(result);
    assertEquals(id, result.getId());
}
```

## 🗄️ Migrações de Banco de Dados

### Criar Nova Migration
1. Crie arquivo em `src/main/resources/db/migration/`
2. Nomeie como: `V{número}__{descrição}.sql`
   - Exemplo: `V2__add_categories_table.sql`
3. Use numeração sequencial
4. Nunca modifique migrations já aplicadas

### Exemplo de Migration
```sql
-- V2__add_categories_table.sql
CREATE TABLE IF NOT EXISTS public.categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

ALTER TABLE public.products
ADD COLUMN category_id BIGINT REFERENCES public.categories(id);
```

## 📋 Checklist de PR

Antes de submeter um Pull Request, verifique:

- [ ] Código compila sem erros (`./gradlew build`)
- [ ] Testes passam (`./gradlew test`)
- [ ] Seguiu os padrões de arquitetura
- [ ] Adicionou testes para novas funcionalidades
- [ ] Atualizou documentação (README, comentários)
- [ ] Migrations seguem o padrão de nomenclatura
- [ ] Commit messages são descritivas

## 🐛 Reportar Bugs

Ao reportar um bug, inclua:
- Descrição clara do problema
- Passos para reproduzir
- Comportamento esperado vs. obtido
- Versão do Java e sistema operacional
- Logs de erro (se aplicável)

## 💡 Sugerir Funcionalidades

Para sugerir novas funcionalidades:
1. Verifique se já não existe uma issue relacionada
2. Crie uma issue detalhando:
   - Problema que resolve
   - Solução proposta
   - Alternativas consideradas
   - Impacto no sistema

## ❓ Dúvidas

Se tiver dúvidas:
- Abra uma issue com a tag `question`
- Revise a documentação no README
- Verifique issues fechadas

## 📜 Código de Conduta

- Seja respeitoso e inclusivo
- Aceite críticas construtivas
- Foque no que é melhor para a comunidade
- Mantenha discussões técnicas e profissionais

---

**Obrigado por contribuir! 🎉**
