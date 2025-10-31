# Quick Reference Guide - VZDolci Backend

## 🚀 Getting Started

### Build the Application
```bash
./gradlew clean build -x test
```
Output: `build/libs/app.jar`

### Run Locally
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Run Tests
```bash
./gradlew test
```

### Build Docker Image
```bash
./gradlew clean build -x test
docker build -t vzdolci-backend:latest .
```

### Run Docker Container
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  vzdolci-backend:latest
```

## 📍 Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Health check - returns "Hello, World!" |
| `/actuator/health` | GET | Actuator health check |
| `/api/v1/products` | GET | List all products |
| `/api/v1/products?activeOnly=true` | GET | List active products only |
| `/api/v1/products/{id}` | GET | Get product by ID |

## 🏗️ Project Structure

```
src/main/java/com/vzdolci/backend/
├── domain/              # Pure business logic (no dependencies)
│   ├── model/          # Product
│   └── repository/     # ProductRepository (interface)
│
├── application/         # Business rules
│   ├── dto/            # ProductResponse, ErrorResponse
│   ├── usecase/        # GetAllProductsUseCase, GetProductByIdUseCase
│   ├── mapper/         # ProductMapper
│   └── exception/      # NotFoundException
│
├── infrastructure/      # Framework implementations
│   └── persistence/
│       ├── entity/     # ProductEntity (JPA)
│       └── repository/ # ProductJpaRepository, ProductRepositoryImpl
│
└── web/                # HTTP layer
    └── controller/     # Controllers, Exception Handler
```

## 🔍 Adding a New Feature (Example: Create Product)

### 1. Create DTO (application/dto/)
```java
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    // getters/setters
}
```

### 2. Add Repository Method (if needed)
Already exists: `Product save(Product product);`

### 3. Create Use Case (application/usecase/)
```java
@Service
public class CreateProductUseCase {
    private final ProductRepository repository;
    
    public Product execute(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        // ... set other fields
        return repository.save(product);
    }
}
```

### 4. Add Controller Endpoint (web/controller/)
```java
@PostMapping
public ResponseEntity<ProductResponse> createProduct(
        @RequestBody CreateProductRequest request) {
    Product product = createProductUseCase.execute(request);
    return ResponseEntity.ok(ProductResponse.fromDomain(product));
}
```

## 🧪 Testing

### Unit Test Example
```java
class GetProductByIdUseCaseTest {
    @Mock
    private ProductRepository repository;
    
    @InjectMocks
    private GetProductByIdUseCase useCase;
    
    @Test
    void shouldReturnProduct() {
        // Given
        Product product = new Product(1L, "Brownie", ...);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        
        // When
        Product result = useCase.execute(1L);
        
        // Then
        assertEquals("Brownie", result.getName());
    }
}
```

## 📦 Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | - |
| `DB_HOST` | Database host | localhost |
| `DB_PORT` | Database port | 5432 |
| `DB_NAME` | Database name | vzdolci |
| `DB_USER` | Database user | postgres |
| `DB_PASSWORD` | Database password | - |
| `PORT` | Application port | 8080 |

## 🎯 Key Principles

### Dependency Rule
- **Outer layers depend on inner layers**
- **Inner layers never depend on outer layers**
- **Domain has no dependencies**

### Layer Responsibilities
- **Domain**: Business entities and rules
- **Application**: Use cases and business logic
- **Infrastructure**: Framework and external integrations
- **Web**: HTTP requests/responses

### SOLID Quick Reference
- **S**ingle Responsibility: One reason to change
- **O**pen/Closed: Open for extension, closed for modification
- **L**iskov Substitution: Subtypes are substitutable
- **I**nterface Segregation: Small, focused interfaces
- **D**ependency Inversion: Depend on abstractions

## 📚 Documentation

- `CLEAN_ARCHITECTURE.md` - Detailed architecture guide
- `DOCKER_DEPLOYMENT.md` - Deployment instructions
- `REFACTORING_SUMMARY.md` - Summary of all changes
- `TRANSFORMATION.md` - Before/after comparison
- `ARCHITECTURE.md` - Original architecture doc
- `QUICK-START.md` - Quick start guide

## 🐛 Troubleshooting

### Build fails
```bash
./gradlew clean build --stacktrace
```

### Tests fail
```bash
./gradlew test --info
```

### Port already in use
```bash
# Change port
export PORT=8081
./gradlew bootRun
```

### Database connection error
- Check `docker-compose.yml` is running
- Verify environment variables
- Check `application-dev.yml` configuration

## 💡 Tips

1. **Always run tests** before committing
2. **Use DTOs** for API contracts, not domain models
3. **Keep controllers thin** - delegate to use cases
4. **Mock repositories** in use case tests
5. **Follow the dependency rule** - inner layers don't know about outer layers

## 🔗 Useful Commands

```bash
# Clean build
./gradlew clean build -x test

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Generate bootJar only
./gradlew bootJar

# Run Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f

# Stop everything
docker-compose down
```

## 📞 Getting Help

1. Check documentation in project root
2. Review code examples in existing use cases
3. Run tests to see expected behavior
4. Check `CLEAN_ARCHITECTURE.md` for patterns
