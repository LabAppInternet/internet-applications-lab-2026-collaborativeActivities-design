<!-- SOURCE OF TRUTH · edit this file, not the HTML -->
<!-- disseny/*.html is frozen as a style guide -->

# Learning Outcomes of the Internet Applications Laboratory

*List of the content that students must learn and apply*

Exhaustive list of the competencies, technologies, patterns and skills that students will develop during the course. It does not include fundamental programming concepts (loops, conditionals, data structures) or the superficial use of Spring Boot that they already have.

---

## Persistence and data access

### JPA and Hibernate

- **Domain entities** as annotated classes: `@Entity`, `@Table`, `@Column`
- **Entity lifecycle**: transient, managed, detached, removed
- **Relationships**: `@OneToMany`, `@ManyToOne`, `@ManyToMany` with direction and owner
- **Composite keys** with `@Embeddable` and `@EmbeddedId`
- **Optimistic versioning** with `@Version` (if implemented)
- **Queries** with JPQL and `@Query` for complex cases
- **Lazy loading** vs eager loading and the performance implications
- **Flush and synchronization** with the database inside transactions

### Spring Data JPA

- **Repositories** as data-access abstractions: `extends JpaRepository<T, ID>`
- **Query methods** generated automatically: `findBy*`, `deleteBy*`, etc.
- **Pagination**: `Page<T>`, `Pageable`, sorts
- **Cursor pagination** as an alternative for large data sets
- **Batch operations**: bulk insert and update

### Transactions and ACID

- **Explicit transactions** with `@Transactional`
- **Isolation levels** and their implications: serializable, repeatable read, read committed, read uncommitted
- **Concurrency problems**: dirty read, non-repeatable read, phantom read, lost update, write skew
- **Manual rollback** and `@Transactional(rollbackFor=...)`
- **Transaction management in the service layer**, not the controller

---

## Concurrency in Java applications

### Conditional update

- **Conditional UPDATE pattern**: decide by the number of affected rows
- **Conflict detection** without locking: `executeUpdate()` and checking the result
- **Retry logic** when conflicts are detected
- **Trade-off between simplicity and optimism** when choosing concurrency mechanisms

### Thread-safe usage

- **Thread safety in a Spring context**: beans are singletons by default
- **Immutability** as a thread-safety strategy
- **ThreadLocal** when necessary (e.g. the authenticated user's context)
- **Race conditions** in read-modify-write
- **Volatile** and `volatile` variables for visibility between threads

### Concurrency testing

- **JUnit with concurrency**: fixtures to simulate multiple simultaneous threads
- **Timing assertions** in concurrent tests
- **Race condition detection**: determinism cannot be guaranteed in parallel tests

---

## Application security

### Authentication

- **Stateless authentication** with JWT (JSON Web Tokens)
- **JWT structure and validation**: header, payload, signature
- **Standard and custom claims**
- **Token expiration** and refresh
- **Secure storage of secrets**

### Authorization

- **Role-based access control (RBAC)**: assigning roles to users
- **Permissions** and verification at endpoints
- **Fine-grained authorization**: cross-access by role
- **Separation of concerns**: separating authentication from authorization

### Spring Security

- **Security filters** in the Spring chain
- **Authentication providers** for custom strategies
- **Authorization rules** with SpEL expressions
- **CORS** and handling of cross-origin requests
- **CSRF** (Cross-Site Request Forgery) when relevant

### Data validation

- **Validation at the API boundary** (input) vs internal (domain) validation
- **Constraints** with annotations: `@NotNull`, `@Email`, `@Size`, etc.
- **Custom validators** for complex validation logic
- **Structured error responses** in RFC 9457 format (problem+json)
- **Difference between `422 Unprocessable Entity` and `400 Bad Request`**

---

## REST API design

### Design principles

- **Plural nouns as resources**: `/events`, `/performances`, `/orders`
- **Subresources** for actions that are not CRUD: `POST /events/{id}/publication`
- **Structural scoping**: `/me/orders` vs `/orders?mine=true`
- **Idempotency** in operations that charge money: `Idempotency-Key`
- **Cursor pagination**: `/orders?limit=20&after=eyJpZCI...`

### Responses and status codes

- **HTTP status codes with purpose**: `201` with Location, `204`, `409`, `422`, `404`
- **Structured error responses**: RFC 9457 `application/problem+json`
- **Identifiable and stable error type URIs**
- **Additional fields** in error responses (e.g. `unavailableSeats`)

### Content negotiation

- **Content-Type** and `Accept` headers
- **JSON serialization**: Jackson or an equivalent framework
- **Date/time conversion** to ISO 8601 format

---

## Architecture patterns

### Layered separation

- **Controller layer**: format validation, delegation to the service
- **Service layer**: business logic, orchestration of repositories
- **Repository layer**: data access, queries
- **Model layer**: rich entities, domain logic

### Rich domain entities

- **Behavior encapsulated** in the entity: transaction denial, state changes
- **Domain invariants** protected by methods
- **Private setters** for fields that are only modified through behavior
- **Value objects** for concepts whose identity comes from their value, not from an ID

### Composition vs inheritance

- **Preference for composition**: `Embedded` and `Embeddable` instead of JPA inheritance
- **TABLE_PER_CLASS strategy** if multiple types are needed, but prefer composition

---

## System-specific operations

### Purchase order and idempotency

- **Idempotency keys**: keep determinism under retries
- **Virtual duplicates**: detect when a request is a repetition
- **Consistent response**: return the same response for the same key

### Ticket validation

- **Double-scanning**: it must be impossible to validate the same ticket twice
- **Conditional checks** based on the previous state
- **Atomic operations**: validate and change state in a single query

### Seat reservation

- **Extreme concurrency**: 50+ threads trying to reserve simultaneously
- **Selection of available seats**: conditional update operations
- **Automatic rollback** when the seats are not available

### High-volume memory

- **Query optimization** for 20,000+ records
- **Streaming or pagination** instead of loading everything into memory
- **Database indexes** for performance

---

## Testing and quality

### Unit tests

- **Mocking** of repositories in service tests
- **Fixtures** and test data
- **Assertions** on domain behavior

### Integration tests

- **TestContainers** or `@DataJpaTest` for tests with a real database
- **Transactional tests**: automatic rollback between tests
- **Setup and teardown data**

### Acceptance tests

- **Provided test suite** against the HTTP API
- **Specific test cases**: concurrency, idempotency, cross-access
- **Performance analysis**: number of SQL statements, memory consumption

### Debugging and profiling

- **Logging at levels** (DEBUG, INFO, WARN, ERROR)
- **Analysis of the generated SQL queries**
- **JVM profilers** for performance bottlenecks

---

## Tools and configuration

### Maven

- **`pom.xml`**: dependencies, plugins, profiles
- **Build lifecycle**: compile, test, package
- **Spring Boot Maven Plugin** to run the application

### PostgreSQL database

- **Connection** within Spring Boot
- **Connection pooling**: HikariCP
- **Migrations** with Flyway or Liquibase
- **Indexes** and explained queries

### Git and versioning

- **Atomic commits** for each conceptual change
- **Branches** for features
- **Pull requests** with a clear description

### Profiling and monitoring

- **Spring Boot Actuator endpoints**
- **Basic metrics**: request count, latency
- **Custom health checks**

---

## Soft skills and methodology

### Teamwork

- **Resolution of merge conflicts**
- **Code review** among peers
- **Task estimation**
- **Management of dependencies** between members

### Technical communication

- **Writing clear use cases**
- **Documentation** of design decisions
- **Explanation** of architectural trade-offs

### Incremental learning

- **Progressive construction**: simple use cases first, gradual complexity
- **Continuous feedback** from acceptance tests
- **Refactoring** as complexity grows

---

## Third-year topics deliberately excluded

The following topics are not part of the course and are left for later courses or specializations:

- **Microservices** and distributed systems: monolithic system by design
- **Hexagonal architecture** (ports and adapters): layered architecture is sufficient
- **Distributed caching**: Redis or equivalent is outside the course
- **Event sourcing** or CQRS: standard ORM models
- **Secrets management** (vaults, rotation): simple configuration with `application.properties`
- **Advanced monitoring** and distributed tracing: basic logging is sufficient
- **API Gateway** and rate limiting: a deployment responsibility, not the app's
- **GraphQL**: REST is the standard for this course
- **Streaming** (e.g. WebSockets): synchronous HTTP operations only
