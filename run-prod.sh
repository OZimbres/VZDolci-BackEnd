#!/bin/bash
echo "🚀 Iniciando VZDolci Backend (Produção)"
echo "🔍 Verificando variáveis de ambiente..."

required_vars=("SUPABASE_HOST" "SUPABASE_PORT" "SUPABASE_DB" "SUPABASE_USER" "SUPABASE_PASSWORD")
missing_vars=()

for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        missing_vars+=("$var")
    fi
done

if [ ${#missing_vars[@]} -ne 0 ]; then
    echo "❌ Erro: As seguintes variáveis de ambiente não estão definidas:"
    printf '   - %s\n' "${missing_vars[@]}"
    echo ""
    echo "Configure as variáveis antes de executar:"
    echo "  export SUPABASE_HOST=db.xxxxxx.supabase.co"
    echo "  export SUPABASE_PORT=5432"
    echo "  export SUPABASE_DB=postgres"
    echo "  export SUPABASE_USER=postgres"
    echo "  export SUPABASE_PASSWORD=sua_senha"
    exit 1
fi

echo "✅ Todas as variáveis de ambiente configuradas"
echo "🏗️  Compilando aplicação..."
./gradlew clean build -x test

echo "▶️  Iniciando aplicação com perfil de produção..."
./gradlew bootRun --args='--spring.profiles.active=prod'
