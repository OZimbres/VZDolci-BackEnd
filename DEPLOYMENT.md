# Guia de Deploy - VZDolci Backend

Este guia fornece instruções detalhadas para fazer o deploy da aplicação em diferentes plataformas.

## 📋 Pré-requisitos

- Conta no Supabase configurada
- Credenciais do banco de dados Supabase
- JAR da aplicação compilado

## 🗄️ Configuração do Supabase

### 1. Criar Projeto no Supabase

1. Acesse [app.supabase.com](https://app.supabase.com/)
2. Clique em "New Project"
3. Preencha:
   - **Project name**: vzdolci-backend
   - **Database Password**: (escolha uma senha forte)
   - **Region**: South America (São Paulo) ou mais próxima
4. Aguarde a criação (~2 minutos)

### 2. Obter Credenciais

1. No painel do projeto, vá em **Settings → Database**
2. Na seção "Connection info", copie:
   - **Host**: `db.xxxxxx.supabase.co`
   - **Port**: `5432`
   - **Database**: `postgres`
   - **User**: `postgres`
   - **Password**: (a senha que você definiu)

### 3. Connection Pooling (Recomendado)

Para melhor performance, use o pooling host:
- Em **Connection Pooling**, copie o host: `pgbouncer.xxxxxx.supabase.co`
- Use esse host no lugar do direto

### 4. Testar Conexão

```bash
psql "postgresql://postgres:sua_senha@db.xxxxxx.supabase.co:5432/postgres?sslmode=require"
```

## 🚀 Deploy em Diferentes Plataformas

### Railway

Railway é recomendado pela facilidade e integração nativa com PostgreSQL.

#### Passos:

1. **Criar conta no [Railway](https://railway.app/)**

2. **Novo Projeto**
   - Clique em "New Project"
   - Selecione "Deploy from GitHub repo"
   - Autorize o acesso ao repositório
   - Selecione `OZimbres/VZDolci-BackEnd`

3. **Configurar Variáveis de Ambiente**
   - No painel do projeto, clique em "Variables"
   - Adicione:
     ```
     SPRING_PROFILES_ACTIVE=prod
     SUPABASE_HOST=db.xxxxxx.supabase.co
     SUPABASE_PORT=5432
     SUPABASE_DB=postgres
     SUPABASE_USER=postgres
     SUPABASE_PASSWORD=sua_senha
     ```

4. **Configurar Build**
   - Railway detecta automaticamente o Gradle
   - Build command: `./gradlew clean build -x test`
   - Start command: `java -jar build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar`

5. **Deploy**
   - Railway fará deploy automaticamente
   - URL será fornecida: `https://vzdolci-backend-production.up.railway.app`

#### Configuração Avançada

Crie `railway.json` na raiz:
```json
{
  "build": {
    "builder": "NIXPACKS",
    "buildCommand": "./gradlew clean build -x test"
  },
  "deploy": {
    "startCommand": "java -Dserver.port=$PORT -jar build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar",
    "healthcheckPath": "/actuator/health",
    "healthcheckTimeout": 100,
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### Render

#### Passos:

1. **Criar conta no [Render](https://render.com/)**

2. **Novo Web Service**
   - Dashboard → "New +" → "Web Service"
   - Conecte GitHub e selecione o repositório
   
3. **Configurações**
   - **Name**: `vzdolci-backend`
   - **Environment**: `Docker` ou `Java`
   - **Build Command**: `./gradlew clean build -x test`
   - **Start Command**: `java -jar build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar`

4. **Variáveis de Ambiente**
   Adicione em "Environment":
   ```
   SPRING_PROFILES_ACTIVE=prod
   SUPABASE_HOST=db.xxxxxx.supabase.co
   SUPABASE_PORT=5432
   SUPABASE_DB=postgres
   SUPABASE_USER=postgres
   SUPABASE_PASSWORD=sua_senha
   ```

5. **Deploy**
   - Clique em "Create Web Service"
   - Render fará deploy automaticamente

#### Dockerfile para Render (Opcional)

Crie `Dockerfile`:
```dockerfile
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Heroku

#### Passos:

1. **Instalar Heroku CLI**
```bash
# macOS
brew install heroku/brew/heroku

# Ubuntu/Debian
curl https://cli-assets.heroku.com/install.sh | sh
```

2. **Login**
```bash
heroku login
```

3. **Criar App**
```bash
heroku create vzdolci-backend
```

4. **Configurar Variáveis**
```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set SUPABASE_HOST=db.xxxxxx.supabase.co
heroku config:set SUPABASE_PORT=5432
heroku config:set SUPABASE_DB=postgres
heroku config:set SUPABASE_USER=postgres
heroku config:set SUPABASE_PASSWORD=sua_senha
```

5. **Deploy**
```bash
git push heroku main
```

#### system.properties
Crie arquivo `system.properties`:
```properties
java.runtime.version=17
```

#### Procfile
Crie arquivo `Procfile`:
```
web: java -Dserver.port=$PORT -jar build/libs/vzdolci-backend-0.0.1-SNAPSHOT.jar
```

### AWS Elastic Beanstalk

#### Pré-requisitos
- AWS CLI instalado
- Conta AWS configurada

#### Passos:

1. **Build do Projeto**
```bash
./gradlew clean build -x test
```

2. **Inicializar EB**
```bash
eb init -p java vzdolci-backend --region us-east-1
```

3. **Criar Ambiente**
```bash
eb create vzdolci-production
```

4. **Configurar Variáveis**
```bash
eb setenv SPRING_PROFILES_ACTIVE=prod \
  SUPABASE_HOST=db.xxxxxx.supabase.co \
  SUPABASE_PORT=5432 \
  SUPABASE_DB=postgres \
  SUPABASE_USER=postgres \
  SUPABASE_PASSWORD=sua_senha
```

5. **Deploy**
```bash
eb deploy
```

#### .ebextensions/options.config
```yaml
option_settings:
  aws:elasticbeanstalk:application:environment:
    SERVER_PORT: 5000
  aws:elasticbeanstalk:container:java:staticfiles:
    enabled: false
```

### Google Cloud Run

#### Passos:

1. **Criar Dockerfile** (veja exemplo no Render)

2. **Build e Push**
```bash
gcloud builds submit --tag gcr.io/PROJECT_ID/vzdolci-backend
```

3. **Deploy**
```bash
gcloud run deploy vzdolci-backend \
  --image gcr.io/PROJECT_ID/vzdolci-backend \
  --platform managed \
  --region southamerica-east1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_PROFILES_ACTIVE=prod,SUPABASE_HOST=db.xxxxxx.supabase.co,SUPABASE_PORT=5432,SUPABASE_DB=postgres,SUPABASE_USER=postgres,SUPABASE_PASSWORD=sua_senha
```

## 🔍 Verificação de Deploy

Após o deploy, teste os endpoints:

### Health Check
```bash
curl https://seu-app.railway.app/api/v1/products
```

### Teste Completo
```bash
# Listar produtos
curl https://seu-app.railway.app/api/v1/products

# Buscar produto por ID
curl https://seu-app.railway.app/api/v1/products/1

# Teste de erro 404
curl https://seu-app.railway.app/api/v1/products/999
```

## 📊 Monitoramento

### Logs

#### Railway
```bash
railway logs
```

#### Heroku
```bash
heroku logs --tail
```

#### Render
- Acesse o dashboard e clique em "Logs"

### Métricas

Adicione Spring Boot Actuator (já incluído no pom.xml):

Endpoints disponíveis:
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas
- `/actuator/info` - Informações

## 🔒 Segurança em Produção

### 1. CORS
Atualize `ProductController` para restringir origens:
```java
@CrossOrigin(origins = {"https://seusite.com"})
```

### 2. HTTPS
Todas as plataformas mencionadas fornecem HTTPS automaticamente.

### 3. Secrets
**NUNCA** commite senhas no código. Sempre use variáveis de ambiente.

### 4. Rate Limiting
Considere adicionar rate limiting para APIs públicas.

## 🔄 CI/CD

### GitHub Actions

Crie `.github/workflows/deploy.yml`:
```yaml
name: Deploy to Railway

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Build with Gradle
        run: ./gradlew clean build -x test
        
      - name: Deploy to Railway
        run: railway up
        env:
          RAILWAY_TOKEN: ${{ secrets.RAILWAY_TOKEN }}
```

## 🆘 Troubleshooting

### Erro de Conexão com Banco
- Verifique se `sslmode=require` está na URL
- Confirme as credenciais do Supabase
- Teste conexão direta com `psql`

### Timeout na Inicialização
- Aumente o tempo de health check
- Verifique logs de inicialização
- Confirme que Flyway rodou as migrations

### Erro 500 na API
- Verifique logs da aplicação
- Confirme que banco está acessível
- Teste endpoints localmente primeiro

### Out of Memory
- Aumente memória na plataforma
- Otimize pool de conexões Hikari
- Reduza `maximum-pool-size` no `application-prod.yml`

## 📚 Recursos Adicionais

- [Documentação Railway](https://docs.railway.app/)
- [Documentação Render](https://render.com/docs)
- [Documentação Heroku](https://devcenter.heroku.com/)
- [Supabase Docs](https://supabase.com/docs)

---

**Dúvidas?** Abra uma issue no GitHub!
