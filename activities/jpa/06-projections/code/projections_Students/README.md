# Projections: fetch only what the read needs

A product listing needs a product's `name` and `price` — not its long `description`.
Reading the full entity pulls `description` anyway (the **over-fetch**). Projections
select only the columns you ask for.

This project has two projection styles side by side:

- **Interface projection** (`ProductNamePrice` + `findAllNamePrice`) — **done for you**.
  Study it: the aliases `as name` / `as price` map to the getters, and the SQL selects
  only those two columns.
- **Class / DTO projection** (`ProductSummary` record + `findAllSummaries`) — **your
  task**.

## Your task — edit only `ProductRepository.java`

`findAllSummaries` ships with a broken JPQL constructor expression: it projects
`p.description` into the DTO's `name`, so the read returns the wrong text *and* drags
in the heavy `description` column. **Fix the constructor expression to select
`p.name`** (keep `p.price`).

Do not change the entities, `SqlInspector`, the runner, the tests, or
`application.properties`.

## Success signal

```bash
./mvnw test
./mvnw spring-boot:run
```

`ProjectionTest` starts with **`class_projection_selects_only_name_and_price` red**
(the names come back as the long description, and the captured SQL still contains
`description`). The full-entity and interface-projection tests are green. Fix the
constructor expression and all four go green.

`spring-boot:run` prints the SQL each read sends: the full entity selects
`...description..., name, price`; both projections select only `name, price`.

## For the peer review

You will judge another team's projection against the rubric in the activity sheet:
did they remove the over-fetch (only needed columns selected)? Is the projection kind
justified (interface for a read-only view, class/record when you want an immutable
concrete type)? Is the read still correct?

## Notes

- `SqlInspector` is a Hibernate `StatementInspector` that records the SQL so the tests
  can check which columns are selected. It is given — you don't build it.
- Build/run with **JDK 21** (the project targets Java 21).
