# Docker Deployment Guide

This guide explains how to build and deploy the VZDolci Backend using Docker.

## Building the Docker Image

### Prerequisites
- Docker installed on your machine
- Java 17 (for building the JAR file)

### Step 1: Build the JAR file
```bash
./gradlew clean build -x test
```

This will create `build/libs/app.jar`

### Step 2: Build the Docker image
```bash
docker build -t vzdolci-backend:latest .
```

### Step 3: Run the container
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-db-host \
  -e DB_PORT=5432 \
  -e DB_NAME=vzdolci \
  -e DB_USER=your-user \
  -e DB_PASSWORD=your-password \
  vzdolci-backend:latest
```

## Environment Variables

The application supports the following environment variables:

- `SPRING_PROFILES_ACTIVE`: Active Spring profile (dev, prod)
- `DB_HOST`: PostgreSQL host
- `DB_PORT`: PostgreSQL port (default: 5432)
- `DB_NAME`: Database name
- `DB_USER`: Database user
- `DB_PASSWORD`: Database password
- `PORT`: Application port (default: 8080)

## Health Checks

The application exposes the following health check endpoints:

- `/`: Basic health check returning "Hello, World!"
- `/actuator/health`: Spring Boot Actuator health endpoint

## Google Cloud Run Deployment

### Build for Cloud Run
```bash
# Build the JAR
./gradlew clean build -x test

# Build and push to Google Container Registry
gcloud builds submit --tag gcr.io/PROJECT_ID/vzdolci-backend

# Deploy to Cloud Run
gcloud run deploy vzdolci-backend \
  --image gcr.io/PROJECT_ID/vzdolci-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_PROFILES_ACTIVE=prod,DB_HOST=... \
  --port 8080
```

### Using Cloud Build (CI/CD)

Create `cloudbuild.yaml`:
```yaml
steps:
  # Build JAR
  - name: 'gradle:7.6-jdk17'
    entrypoint: './gradlew'
    args: ['clean', 'build', '-x', 'test']
  
  # Build Docker image
  - name: 'gcr.io/cloud-builders/docker'
    args: ['build', '-t', 'gcr.io/$PROJECT_ID/vzdolci-backend', '.']
  
  # Push to Container Registry
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/$PROJECT_ID/vzdolci-backend']
  
  # Deploy to Cloud Run
  - name: 'gcr.io/cloud-builders/gcloud'
    args:
      - 'run'
      - 'deploy'
      - 'vzdolci-backend'
      - '--image'
      - 'gcr.io/$PROJECT_ID/vzdolci-backend'
      - '--region'
      - 'us-central1'
      - '--platform'
      - 'managed'
      - '--allow-unauthenticated'

images:
  - 'gcr.io/$PROJECT_ID/vzdolci-backend'
```

## Multi-Stage Docker Build (Optional)

For a more optimized image, you can use a multi-stage build:

```dockerfile
# Build stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Runtime stage
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/build/libs/app.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Testing the Docker Image Locally

```bash
# Build the image
docker build -t vzdolci-backend:test .

# Run with docker-compose (includes PostgreSQL)
docker-compose up

# Test the health endpoint
curl http://localhost:8080/
# Expected: "Hello, World!"

# Test the actuator health endpoint
curl http://localhost:8080/actuator/health
# Expected: {"status":"UP"}

# Test the products API
curl http://localhost:8080/api/v1/products
```

## Troubleshooting

### Container exits immediately
- Check environment variables are set correctly
- View logs: `docker logs <container-id>`

### Cannot connect to database
- Verify database connection settings
- Ensure database is accessible from container
- For Cloud Run, use Cloud SQL Proxy or private VPC

### Port already in use
- Change the port mapping: `-p 8081:8080`

### Health check fails
- Check application logs
- Verify database connection
- Test endpoints manually: `curl http://localhost:8080/actuator/health`

## Production Considerations

1. **Use secrets management**: Don't hardcode credentials
   - Cloud Run: Use Secret Manager
   - Kubernetes: Use Secrets
   - Docker Compose: Use `.env` files (gitignored)

2. **Set resource limits**:
   ```bash
   docker run -m 512m --cpus 1 vzdolci-backend:latest
   ```

3. **Enable health checks**:
   ```yaml
   healthcheck:
     test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
     interval: 30s
     timeout: 10s
     retries: 3
   ```

4. **Use proper logging**:
   - Application logs to stdout/stderr
   - Use log aggregation (Cloud Logging, ELK, etc.)

5. **Monitor the application**:
   - Use `/actuator/metrics` for Prometheus
   - Set up alerting for health check failures
