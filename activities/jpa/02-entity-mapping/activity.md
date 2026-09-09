# Mapping your first rich entity

| | |
|---|---|
| **Cooperative technique** | Think–Act–Review (pair programming with alternating roles) |
| **Learning content** | *Entitats de domini com a classes anotades (`@Entity`, `@Table`, `@Column`)*; *`@GeneratedValue`*; *Composició vs herència (`@Embeddable`)*; *Repositoris `extends JpaRepository`*; *Query methods `findBy*`* (`basePartida/aprendizajes.md`) |
| **Week / level** | Week 2 · small group · "session 2 — first entity mapping" (`009`) |
| **Group setting** | Small group (30, 4 h) |
| **Duration** | 90 min |
| **Team size / roles** | Pairs. Alternating roles: **Driver** (types) and **Navigator** (reads the SQL log, checks against the goal). Swap at each phase boundary. |

## Why this technique here

Mapping is a skill, not a fact — it is learned by doing it under immediate feedback.
Think–Act–Review puts the Navigator on the **SQL log** while the Driver writes the
mapping, so the pair catches a wrong column or a missing `@Id` the moment Hibernate
complains, and the habit from A1 (read the SQL) becomes the review mechanism.

## Learning objectives

By the end, students can:
- Turn a plain class into a persistent entity: `@Entity`, `@Id`, `@GeneratedValue`,
  `@Column`, and an explicit `@Table` name.
- Model a small value concept as an `@Embeddable` **value object** held by
  composition, and explain why that is not entity inheritance.
- Read an entity back with a **derived query** (`findBy…`) and confirm the generated
  SQL matches what they intended.

## Prerequisites

- A1 (entity lifecycle, reading the SQL log).
- The seed project imported and running.

## Materials and scaffolding

Code scaffold — **built and verified** (Temurin JDK 21, offline):
`code/entitymapping_Students/` (hand-out) and `code/entitymapping_Solution/`
(reference, not distributed). Boot 3.4.5, Java 21, H2 in-memory, package
`cat.tecnocampus.entitymapping`, SQL logging on.

- `Product` and its `Price` value class ship as **plain classes**; `ProductRepository`
  (with the derived query `findByNameContaining` as a worked example) and a
  `DemoRunner` are given.
- The gap is the **mapping**, marked `// TODO(student)` in both files:
  - `Product` → `@Entity`, `@Table("products")`, `@Id @GeneratedValue`, `@Embedded`
  - `Price` → `@Embeddable` (a value object; no `@Id` — composition, not inheritance)
- Success signal: `./mvnw test`. Until the mapping is added, every test errors with
  **`Not a managed type: … Product`** (a repository can't be built for a non-entity) —
  the same red the students met in the existing `jpa/*_Students` exercises. Once mapped,
  `contextLoads` and `ProductMappingTest` pass: generated id, the embedded price
  round-trips, and `findByNameContaining("Radio")` returns exactly one row. The run
  prints one `insert into products` per product (price columns in the same table).
- Scope: students edit only `Product` and `Price`. The derived query is provided; a
  second one of their own is an optional extension.

> Build with **JDK 21** — JDK 25 can trip the Hibernate/Byte Buddy agent at test time.
> Note the derived query is a worked example, not a gap: a derived query has no method
> body to leave blank, so the mapping is what students complete. Do not touch
  the runner or `application.properties`.

## Sequence

| Phase | Min | Grouping | Roles active | What happens |
|-------|-----|----------|--------------|--------------|
| Setup | 10 | whole class | teacher | Recap the lifecycle; show the failing runner; assign Driver/Navigator. |
| Act 1 — entity | 25 | pairs | Driver+Navigator | Map `Product` as an entity; run; Navigator confirms the insert SQL. |
| Review 1 | 5 | pairs → class | teacher | Two pairs show their `@Table`/`@Column` choices; name common errors. |
| Swap + Act 2 — value object | 25 | pairs | roles swapped | Embed `Price` as a value object; run; confirm columns land in the `products` table (no extra table). |
| Act 3 — derived read | 15 | pairs | Driver+Navigator | Add the derived query; confirm the generated `where` clause; make the `@DataJpaTest` pass. |
| Debrief | 10 | whole class | teacher | Why composition here (not inheritance); how the method name became SQL; who translated it (Spring Data). |

<!-- 10+25+5+25+15+10 = 90 min -->

## Deliverable

The completed project: runner prints both products with ids, the value object maps
into the same table, and the provided `@DataJpaTest` passes. Pushed to the team repo.

## Assessment

- **Formative:** Navigators' running commentary on the SQL log is the live signal;
  the teacher listens for pairs who add `@Column` without checking the emitted DDL.
- **Evidence of learning:** the embedded value object produces columns on `products`
  (not a second table), and the derived query's `where` clause matches its method
  name.

## Teacher notes

- **Common pitfalls:** forgetting `@GeneratedValue` (ids come back null); expecting
  `@Embeddable` to create a table; a derived-query method name that doesn't match a
  field, producing a startup error rather than a runtime one.
- **Timing risk:** Act 2 overruns if pairs gold-plate the value object; keep it to
  two fields. *90 min is an estimate, not measured — a candidate for N0 timing.*
- **Constraint check:** rich entity (behaviour/invariants can live on `Product`),
  composition via `@Embeddable`, no inheritance, no concurrency mechanism. Repository
  present; keep any orchestration in a service, not the runner, once the project
  grows.
- **Differentiation:** fast pairs add an invariant to the value object (reject a
  negative price in its constructor) — a first taste of a rich domain entity.
