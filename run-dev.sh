#!/bin/bash
echo "🚀 Iniciando VZDolci Backend (Desenvolvimento)"
echo "📦 Verificando se o Docker está rodando..."

if docker ps > /dev/null 2>&1; then
    echo "✅ Docker disponível"
    echo "🐘 Iniciando PostgreSQL..."
    docker-compose up -d
    echo "⏳ Aguardando PostgreSQL inicializar..."
    sleep 5
else
    echo "⚠️  Docker não disponível, certifique-se de ter PostgreSQL rodando localmente"
fi

echo "🏗️  Compilando aplicação..."
./gradlew clean build -x test

echo "▶️  Iniciando aplicação..."
./gradlew bootRun --args='--spring.profiles.active=dev'
