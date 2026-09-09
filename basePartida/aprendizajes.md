<!-- FONT DE VERITAT · edita aquest fitxer, no l'HTML -->
<!-- disseny/*.html esta congelat com a guia d'estil -->

# Aprenentatges de Laboratori d'Aplicacions Internet

*Llistat dels continguts que els estudiants han d'aprendre i aplicar*

Relació exhaustiva de les competències, tecnologies, patrons i habilitats que els estudiants desenvoluparan durant l'assignatura. No inclou conceptes de programació fonamental (bucles, condicionals, estructures de dades) ni ús superficial de Spring Boot que ja posseeixen.

---

## Persistència i accés a dades

### JPA i Hibernate

- **Entitats de domain** com a classes anotades: `@Entity`, `@Table`, `@Column`
- **Cicle de vida de les entitats**: transient, managed, detached, removed
- **Relacions**: `@OneToMany`, `@ManyToOne`, `@ManyToMany` amb direccionament i propietari
- **Composite keys** amb `@Embeddable` i `@EmbeddedId`
- **Versionat optimista** amb `@Version` (si s'implementa)
- **Queries** amb JPQL i `@Query` per a casos complexos
- **Lazy loading** vs eager loading i les implicacions de rendiment
- **Flush i sincronització** amb la base de dades dentro de transaccions

### Spring Data JPA

- **Repositoris** com a abstraccions del accés a dades: `extends JpaRepository<T, ID>`
- **Query methods** generats automàticament: `findBy*`, `deleteBy*`, etc.
- **Paginació**: `Page<T>`, `Pageable`, comparadores (sorts)
- **Paginació per cursor** com a alternativa per a conjunts grans
- **Batch operations**: inserció i actualització en massa

### Transaccions i ACID

- **Transaccions explícites** amb `@Transactional`
- **Nivells d'aïllament** i les seves implicacions: serializable, repeatable read, read committed, read uncommitted
- **Problemes de concurrència**: dirty read, non-repeatable read, phantom read, lost update, write skew
- **Rollback manual** i `@Transactional(rollbackFor=...)`
- **Gestió de transaccions en capa de servei** no de controlador

---

## Concurrència en aplicacions Java

### Actualització condicional

- **Patró d'UPDATE condicional**: decidir pel nombre de files afectades
- **Detecció de conflictes** sense bloqueig: `executeUpdate()` i control del resultat
- **Retry logic** quan es detecten conflictes
- **Trade-off entre simplicitat i optimisme** al escollir mecanismes de concurrència

### Ús segur de threads

- **Thread safety en context de Spring**: per defecte els beans són singletons
- **Immutabilitat** com estratègia de seguretat en threads
- **ThreadLocal** quan és necessari (ex: context del usuari autenticat)
- **Race conditions** en lectura-modificació-escriptura
- **Volatile** i `volatile` variables per a visibilitat entre threads

### Proves de concurrència

- **JUnit amb concurrència**: fixtures per a simular múltiples threads simultanis
- **Assertions temporals** en tests concurrents
- **Detecció de race conditions**: no garantir determinisme en tests paral·lels

---

## Seguretat de les aplicacions

### Autenticació

- **Estateless authentication** amb JWT (JSON Web Tokens)
- **Estructura i validació de JWT**: header, payload, signature
- **Claims** estàndard i personalitzats
- **Expiració de tokens** i refresc
- **Emmagatzemament de secrets** segur

### Autorització

- **Rol-based access control (RBAC)**: assignment de rols als usuaris
- **Permissions** i verificació en endpoints
- **Fine-grained authorization**: accés cruzat per rol
- **Separation of concerns**: separació entre autenticació i autorització

### Spring Security

- **Filtres de seguretat** en la cadena de Spring
- **Authentication providers** per a estratègies custom
- **Authorization rules** amb expressions SpEL
- **CORS** i gestió de solicituds cross-origin
- **CSRF** (Cross-Site Request Forgery) quan és rellevant

### Validació de dades

- **Validació al límit de la API** (entrada) vs validació interna (domini)
- **Constraints** amb annotations: `@NotNull`, `@Email`, `@Size`, etc.
- **Custom validators** per a lògica de validació complexa
- **Error responses estructurats** en format RFC 9457 (problem+json)
- **Diferència entre `422 Unprocessable Entity` i `400 Bad Request`**

---

## Disseny d'APIs REST

### Principis de disseny

- **Recursos en plural i substantius**: `/events`, `/performances`, `/orders`
- **Subrecursos** per a accions que no són CRUD: `POST /events/{id}/publication`
- **Alcance estructural**: `/me/orders` vs `/orders?mine=true`
- **Idempotència** en operacions que cobren: `Idempotency-Key`
- **Paginació per cursor**: `/orders?limit=20&after=eyJpZCI...`

### Respostes i codis d'estat

- **HTTP status codes amb propòsit**: `201` amb Location, `204`, `409`, `422`, `404`
- **Structured error responses**: RFC 9457 `application/problem+json`
- **Error type URIs** identificables i estables
- **Additional fields** en respostes d'error (ex: `unavailableSeats`)

### Negociació de contingut

- **Content-Type** i `Accept` headers
- **Serialització JSON**: Jackson o framework equivalent
- **Conversió de date/time** a format ISO 8601

---

## Patrons d'arquitectura

### Separació per capes

- **Capa de controlador**: validació de format, delegació a servei
- **Capa de servei**: lògica de negoci, orquestració de repositoris
- **Capa de repositori**: accés a dades, queries
- **Capa de model**: entitats riques, lògica de domini

### Entitats de domini riques

- **Comportament encapsulat** en l'entitat: negació de transaccions, canvis de estat
- **Invariants de domini** protegits per mètodes
- **Private setters** per a camps que només es modifiquen via comportament
- **Value objects** per a conceptes amb identitat única pel valor, no per ID

### Composició vs herència

- **Preferència per composició**: `Embedded` i `Embeddable` en lloc d'herència JPA
- **Estratègia TABLE_PER_CLASS** si cal múltiples tipus, però preferir composició

---

## Operacions específiques del sistema

### Ordre de compra i idempotència

- **Claus d'idempotència**: mantenir determinisme amb reintents
- **Duplicats virtuals**: detectar quan una sol·licitud és una repetició
- **Resposta consistent**: tornar la mateixa resposta amb la mateixa clau

### Validació de tiquets

- **Double-scanning**: impossibilitat de validar dues vegades el mateix tiquet
- **Checks condicionals** en base a l'estat previ
- **Operacions atòmiques**: validar i canviar estat en una sola consulta

### Reserva de butaques

- **Concurrència extrema**: 50+ threads intentant reservar simultàniament
- **Selecció de butaques disponibles**: operacions d'actualització condicionada
- **Rollback automàtic** quan les butaques no són disponibles

### Memòria en gran volum

- **Optimització de queries** per a 20.000+ registres
- **Streaming o paginació** en lloc de carregar tot en memòria
- **Índexs de base de dades** per a performance

---

## Proves i qualitat

### Unit tests

- **Mocking** de repositoris en tests de servei
- **Fixtures** i test data
- **Assertions** sobre el comportament del domini

### Integration tests

- **TestContainers** o `@DataJpaTest` per a proves amb base de dades real
- **Transactional tests**: rollback automàtic entre tests
- **Dades de setup** i teardown

### Acceptance tests

- **Suite de tests proveïda** contra la API HTTP
- **Casos de test determinats**: concurrència, idempotència, accés cruzat
- **Análisis de performance**: nombre de sentències SQL, consum de memòria

### Debugging i profiling

- **Logging a nivells** (DEBUG, INFO, WARN, ERROR)
- **Análisis de queries SQL** generades
- **Profilers** de JVM per a bottlenecks de performance

---

## Eines i configuració

### Maven

- **`pom.xml`**: dependències, plugins, perfils
- **Cicle de build**: compile, test, package
- **Spring Boot Maven Plugin** per a executar aplicació

### Base de dades PostgreSQL

- **Connexió** dins de Spring Boot
- **Connection pooling**: HikariCP
- **Migracions** amb Flyway o Liquibase
- **Índexs** i queries explicades

### Git i versionat

- **Commits atòmics** per a cada canvi conceptual
- **Branches** per a features
- **Pull requests** amb descripció clara

### Profiling i monitorització

- **Actuator endpoints** de Spring Boot
- **Mètriques basiques**: request count, latency
- **Health checks** personalitzats

---

## Soft skills i metodologia

### Treball en equip

- **Resolució de conflictes** de merge
- **Code review** entre companys
- **Estímació** de tasques
- **Gestió de dependències** entre membres

### Comunicació tècnica

- **Redacció de cases d'ús** clars
- **Documentació** de decisions de disseny
- **Explicació** de trade-offs arquitectònics

### Aprenentatge incremental

- **Construcció progressiva**: casos d'ús simples primer, complexitat gradual
- **Feedback continu** de proves de acceptació
- **Refactorització** quan creix la complexitat

---

## Contexts de grau 3 que es descarten deliberadament

Els següents temes no estan dins de l'assignatura i queden per a assignatures posteriors o especialitzacions:

- **Microserveis** i sistemes distribuïts: Sistema monolític per desseny
- **Hexagonal architecture** (ports i adaptadors): Arquitetura per capes és suficient
- **Caching distribuït**: Redis o equivalent queda fora de l'assignatura
- **Event sourcing** o CQRS: Models estàndard ORM
- **Gestió de secrets** (vaults, rotació): Configuració simple amb `application.properties`
- **Monitoring avançat** i tracing distribuït: Logging bàsic és suficient
- **API Gateway** i rate limiting: Responsabilitat del desplegament, no de l'app
- **GraphQL**: REST és l'estàndard d'aquesta assignatura
- **Streaming** (ex: WebSockets): Operacions síncrones HTTP solament
