# Projections — reference solution

Completed version of the `06-projections` scaffold. **Do not distribute** — hand out
`projections_Students/`.

## What differs from the students' copy

Only `ProductRepository.findAllSummaries()`. The constructor expression selects the
right column:

```java
@Query("select new cat.tecnocampus.projections.ProductSummary(p.name, p.price) from Product p")
List<ProductSummary> findAllSummaries();
```

(The students' copy ships `p.description` in the `name` slot.) The interface
projection `findAllNamePrice` is identical in both copies — the worked example.

## Verify

```bash
./mvnw test        # BUILD SUCCESS — 4 tests, 0 failures
./mvnw spring-boot:run
```

Expected SELECT lists in the run output:

```
Full entities:        select ...id, description, name, price... from products
Interface projection: select ...name, price... from products
Class/DTO projection: select ...name, price... from products
```

Verified green with Temurin **JDK 21**, offline.

## Teaching notes

- Only the **class projection** is a red→green gap. An **interface** projection is
  declarative (no body to leave blank), so it is the worked example — students compare
  the two kinds rather than build both.
- `SqlInspector` (a Hibernate `StatementInspector`, registered via
  `hibernate.session_factory.statement_inspector`) is what lets the test assert on the
  selected columns. It's given infrastructure.
- The gap is a *valid* JPQL constructor expression that selects the wrong column, so it
  passes bootstrap validation and fails by assertion (wrong names + `description` in the
  SQL) — not by a startup error. That keeps the other tests green.
- Watch for the hidden trap worth raising in the debrief: a projection that still
  touches a lazy association re-introduces an N+1 (tie back to activity 04).
