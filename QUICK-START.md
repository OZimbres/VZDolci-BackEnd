# 🚀 Quick Start - VZDolci Backend

Guia rápido para iniciar o projeto em menos de 5 minutos.

## ⚡ Início Rápido (Docker)

```bash
# 1. Clone o repositório
git clone https://github.com/OZimbres/VZDolci-BackEnd.git
cd VZDolci-BackEnd

# 2. Inicie o banco de dados
docker-compose up -d

# 3. Execute a aplicação
./run-dev.sh
```

✅ Pronto! API rodando em `http://localhost:8080`

## 🧪 Teste a API

```bash
# Listar todos os produtos
curl http://localhost:8080/api/v1/products

# Buscar produto por ID
curl http://localhost:8080/api/v1/products/1
```

## 📝 Configuração Manual

### Sem Docker

1. **Instale PostgreSQL** (versão 12+)

2. **Crie o banco de dados**
   ```sql
   CREATE DATABASE vzdolci;
   ```

3. **Configure variáveis de ambiente**
   ```bash
   export DB_HOST=localhost
   export DB_PORT=5432
   export DB_NAME=vzdolci
   export DB_USER=postgres
   export DB_PASSWORD=sua_senha
   ```

4. **Execute a aplicação**
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=dev'
   ```

## 🌐 Deploy com Supabase

### 1. Configure o Supabase

- Acesse [app.supabase.com](https://app.supabase.com/)
- Crie um novo projeto
- Anote as credenciais em **Settings → Database**

### 2. Configure variáveis de ambiente

```bash
export SUPABASE_HOST=db.xxxxxx.supabase.co
export SUPABASE_PORT=5432
export SUPABASE_DB=postgres
export SUPABASE_USER=postgres
export SUPABASE_PASSWORD=sua_senha_supabase
```

### 3. Execute com perfil de produção

```bash
./run-prod.sh
```

## 📚 Próximos Passos

1. 📖 Leia o [README.md](README.md) completo
2. 🏗️ Entenda a [ARCHITECTURE.md](ARCHITECTURE.md)
3. 🚀 Veja o guia de [DEPLOYMENT.md](DEPLOYMENT.md)
4. 🤝 Consulte [CONTRIBUTING.md](CONTRIBUTING.md) para contribuir

## 📡 Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/products` | Lista todos os produtos |
| GET | `/api/v1/products?activeOnly=true` | Lista apenas produtos ativos |
| GET | `/api/v1/products/{id}` | Busca produto por ID |

## 🔧 Comandos Úteis

```bash
# Build do projeto
./gradlew clean build

# Executar testes
./gradlew test

# Executar sem testes
./gradlew clean build -x test

# Ver logs
./gradlew bootRun

# Build do JAR
./gradlew bootJar
# Resultado em: build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker Commands

```bash
# Iniciar banco de dados
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar banco de dados
docker-compose down

# Reiniciar (limpar dados)
docker-compose down -v
docker-compose up -d
```

## 📦 Importar Collection de API

1. Abra Postman ou Thunder Client
2. Importe o arquivo `api-collection.json`
3. Configure a base URL se necessário
4. Teste os endpoints!

## ❓ Troubleshooting Rápido

### Erro de conexão com banco
```bash
# Verifique se o PostgreSQL está rodando
docker ps

# Ou (se instalado localmente)
sudo systemctl status postgresql
```

### Erro de compilação
```bash
# Limpe o build e tente novamente
./gradlew clean
./gradlew build
```

### Porta 8080 em uso
```bash
# Mude a porta no application.yml
server:
  port: 8081
```

### Permissão negada nos scripts
```bash
chmod +x run-dev.sh
chmod +x run-prod.sh
chmod +x gradlew
```

## 🎯 Estrutura do Projeto

```
VZDolci-BackEnd/
├── src/
│   ├── main/
│   │   ├── java/com/vzdolci/backend/
│   │   │   ├── domain/          # Modelos de negócio
│   │   │   ├── application/     # Casos de uso
│   │   │   └── infrastructure/  # Controllers, Repositories
│   │   └── resources/
│   │       ├── application.yml  # Configuração base
│   │       └── db/migration/    # Scripts SQL
│   └── test/                    # Testes
├── build.gradle                 # Dependências
├── docker-compose.yml          # PostgreSQL local
├── run-dev.sh                  # Script dev
└── run-prod.sh                 # Script prod
```

## 💡 Dicas

1. **Use Docker** para desenvolvimento local (mais fácil)
2. **Consulte logs** se algo der errado
3. **Teste no Postman** antes de integrar com frontend
4. **Leia ARCHITECTURE.md** para entender o código
5. **Use git branches** para novas features

## 🆘 Precisa de Ajuda?

- 📖 Documentação completa: [README.md](README.md)
- 🏗️ Arquitetura: [ARCHITECTURE.md](ARCHITECTURE.md)
- 🚀 Deploy: [DEPLOYMENT.md](DEPLOYMENT.md)
- 🐛 Problemas: Abra uma issue no GitHub

---

**Bom desenvolvimento! 🎉**
