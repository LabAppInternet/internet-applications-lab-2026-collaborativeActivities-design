# Mapping your first rich entity — reference solution

Completed version of the `02-entity-mapping` scaffold. **Do not distribute** — hand out
`entitymapping_Students/` instead.

## What differs from the students' copy

Only the JPA annotations: `Product` is `@Entity @Table(name = "products")` with an
`@Id @GeneratedValue` and an `@Embedded Price`; `Price` is `@Embeddable`. Everything
else (`ProductRepository`, `DemoRunner`, tests, `application.properties`) is identical.

## Verify

```bash
./mvnw test        # BUILD SUCCESS — 3 tests, 0 failures
./mvnw spring-boot:run
```

Verified green with Temurin **JDK 21** (offline). On JDK 25 the Hibernate/Byte Buddy
agent may complain at test time — build with 21.

## Teaching notes

- `Price` demonstrates **composition over inheritance**: an `@Embeddable` value object
  whose columns (`amount`, `currency`) live on the `products` table — no `@Id`, no
  second table, no entity hierarchy.
- `findByNameContaining` is the worked example of a derived query: the method name is
  the query. Point students at the `where name like ?` in the SQL log.
- The gap is the mapping itself, so the "red" is a context-load failure
  (`Not a managed type`), the same failure mode as the existing `jpa/*_Students`
  exercises — students have seen it before.
