# Clean Architecture Implementation Guide

This document explains the Clean Architecture implementation in the VZDolci Backend project and how SOLID principles are applied throughout the codebase.

## Architecture Overview

The project follows Clean Architecture principles with clear separation of concerns across layers:

```
com/vzdolci/backend/
├── domain/              # Enterprise Business Rules
│   ├── model/          # Business entities (Product)
│   └── repository/     # Repository interfaces
├── application/         # Application Business Rules
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Custom exceptions
│   ├── mapper/         # Domain/Entity mappers
│   └── usecase/        # Use cases (business logic)
├── infrastructure/      # Frameworks & Drivers
│   └── persistence/    
│       ├── entity/     # JPA entities
│       └── repository/ # Repository implementations
└── web/                # Interface Adapters
    └── controller/     # REST controllers
```

## Layer Responsibilities

### Domain Layer (`domain/`)
- **Purpose**: Contains the core business logic and rules
- **Dependencies**: None (completely independent)
- **Key Files**:
  - `Product.java`: Pure domain model without framework dependencies
  - `ProductRepository.java`: Repository interface defining data access contract

### Application Layer (`application/`)
- **Purpose**: Contains use cases and application-specific business logic
- **Dependencies**: Only depends on the domain layer
- **Key Files**:
  - `GetAllProductsUseCase.java`: Business logic for retrieving products
  - `GetProductByIdUseCase.java`: Business logic for retrieving a single product
  - `ProductResponse.java`: DTO for API responses
  - `ProductMapper.java`: Maps between domain and persistence models

### Infrastructure Layer (`infrastructure/`)
- **Purpose**: Implements interfaces defined by inner layers using frameworks
- **Dependencies**: Depends on application and domain layers
- **Key Files**:
  - `ProductEntity.java`: JPA entity with database annotations
  - `ProductJpaRepository.java`: Spring Data JPA repository
  - `ProductRepositoryImpl.java`: Implementation of domain repository interface

### Web Layer (`web/`)
- **Purpose**: Handles HTTP requests and responses
- **Dependencies**: Depends on application layer (use cases and DTOs)
- **Key Files**:
  - `HealthController.java`: Basic health check endpoint
  - `ProductController.java`: REST endpoints for products
  - `GlobalExceptionHandler.java`: Centralized exception handling

## SOLID Principles Applied

### 1. Single Responsibility Principle (SRP)
**"A class should have only one reason to change"**

✅ **Applied**:
- `GetAllProductsUseCase`: Only responsible for retrieving products
- `GetProductByIdUseCase`: Only responsible for retrieving a single product
- `ProductController`: Only handles HTTP concerns, delegates business logic to use cases
- `ProductMapper`: Only responsible for mapping between models
- `HealthController`: Only responsible for health check endpoint

### 2. Open/Closed Principle (OCP)
**"Software entities should be open for extension, but closed for modification"**

✅ **Applied**:
- Repository interfaces allow new implementations without changing existing code
- Use case pattern allows adding new business operations without modifying existing ones
- DTOs can be extended with new response types without changing controllers

**Example**: To add a new payment method, create a `PaymentStrategy` interface:
```java
public interface PaymentStrategy {
    void processPayment(Order order);
}

// New implementations don't require changes to existing code
public class CreditCardPayment implements PaymentStrategy { ... }
public class PixPayment implements PaymentStrategy { ... }
```

### 3. Liskov Substitution Principle (LSP)
**"Subtypes must be substitutable for their base types"**

✅ **Applied**:
- `ProductRepositoryImpl` can be substituted anywhere `ProductRepository` is expected
- Any implementation of repository interfaces behaves consistently

### 4. Interface Segregation Principle (ISP)
**"Clients should not be forced to depend on interfaces they don't use"**

✅ **Applied**:
- `ProductRepository` interface is focused and cohesive
- Controllers only depend on the specific use cases they need
- No "fat" interfaces with unnecessary methods

**Future Enhancement**: Could split into read/write repositories if needed:
```java
public interface ProductReadRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
}

public interface ProductWriteRepository {
    Product save(Product product);
    void deleteById(Long id);
}
```

### 5. Dependency Inversion Principle (DIP)
**"High-level modules should not depend on low-level modules. Both should depend on abstractions"**

✅ **Applied** - This is the core of Clean Architecture:

**Before DIP (❌ Bad)**:
```java
// Use case depends on infrastructure
public class GetProductByIdUseCase {
    private final ProductJpaRepository jpaRepository; // Concrete dependency!
}
```

**After DIP (✅ Good)**:
```java
// Use case depends on domain abstraction
public class GetProductByIdUseCase {
    private final ProductRepository repository; // Interface dependency!
}

// Infrastructure implements domain interface
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;
}
```

**Benefits**:
- Use cases are testable with mock repositories
- Can swap database implementations without changing business logic
- Domain layer remains framework-agnostic

## Dependency Flow

```
┌─────────────────────────────────────┐
│         Web Layer                   │
│  (Controllers, Exception Handler)   │
└───────────────┬─────────────────────┘
                │ depends on
                ↓
┌─────────────────────────────────────┐
│      Application Layer              │
│    (Use Cases, DTOs, Mappers)       │
└───────────────┬─────────────────────┘
                │ depends on
                ↓
┌─────────────────────────────────────┐
│        Domain Layer                 │
│   (Models, Repository Interfaces)   │ ← Pure business logic
└─────────────────────────────────────┘
                ↑
                │ implements
┌───────────────┴─────────────────────┐
│     Infrastructure Layer            │
│  (JPA Entities, Repository Impls)   │
└─────────────────────────────────────┘
```

## Benefits of This Architecture

1. **Testability**: Business logic can be tested independently of frameworks
2. **Maintainability**: Changes in one layer don't affect others
3. **Flexibility**: Can swap implementations (e.g., change from PostgreSQL to MongoDB)
4. **Clear Structure**: New developers can easily understand the codebase
5. **Framework Independence**: Business logic doesn't depend on Spring, JPA, etc.

## Example: Adding a New Feature

To add a "Create Product" feature following this architecture:

### 1. Create DTO (Application Layer)
```java
// application/dto/CreateProductRequest.java
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    // getters/setters
}
```

### 2. Add Method to Repository Interface (Domain Layer)
```java
// domain/repository/ProductRepository.java
Product save(Product product);  // Already exists!
```

### 3. Create Use Case (Application Layer)
```java
// application/usecase/CreateProductUseCase.java
@Service
public class CreateProductUseCase {
    private final ProductRepository repository;
    
    public CreateProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }
    
    public Product execute(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        // Set other fields...
        
        return repository.save(product);
    }
}
```

### 4. Add Controller Endpoint (Web Layer)
```java
// web/controller/ProductController.java
@PostMapping
public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
    Product product = createProductUseCase.execute(request);
    return ResponseEntity.ok(ProductResponse.fromDomain(product));
}
```

## Testing Strategy

### Unit Tests
```java
class GetProductByIdUseCaseTest {
    @Mock
    private ProductRepository repository;
    
    @InjectMocks
    private GetProductByIdUseCase useCase;
    
    @Test
    void shouldReturnProductWhenFound() {
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

### Integration Tests
```java
@SpringBootTest
class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }
}
```

## References

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Google Cloud Run Java Microservice Template](https://github.com/GoogleCloudPlatform/cloud-run-microservice-template-java)
