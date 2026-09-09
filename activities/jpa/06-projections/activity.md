# Projections: fetch only what the read needs

| | |
|---|---|
| **Cooperative technique** | Structured peer review |
| **Learning content** | *Projeccions DTO (interfície i classe)*; *Optimització de queries / evitar over-fetching*; *Queries amb JPQL i `@Query`* (constructor expression) (`basePartida/aprendizajes.md`) |
| **Week / level** | Week 4–5 · small group |
| **Group setting** | Small group (30, 4 h) |
| **Duration** | 90 min |
| **Team size / roles** | Base groups of 4 build; then groups **swap** work. Review roles: **reader**, **rubric-keeper**, **scribe**, **spokesperson**. |

## Why this technique here

Whether a projection is the *right* choice is a judgement, and judgement is sharpened
by evaluating someone else's. Groups first build interface- and class-based
projections, then apply a rubric to **another group's** choice — did they stop the
over-fetch, did they pick the right projection kind, is the read still correct? The
review is where the learning consolidates, because defending or faulting a concrete
choice is harder than reciting the definitions.

## Learning objectives

By the end, students can:
- Implement an **interface-based** projection and a **class/DTO** projection for the
  same read, and show (via the SQL) that only the projected columns are selected.
- Choose between returning a full entity, an interface projection, and a DTO
  projection for a given read, and justify it against over-fetching and mutability.
- Give **structured, rubric-based feedback** on another team's projection choice.

## Prerequisites

- A5 (derived / JPQL / native; JPQL constructor expressions for class projections).
- A4 (the over-fetching mindset: measure what a read pulls).
- Pre-reading (out of class): the existing guide `JPAProjections` (interface vs class,
  dynamic projections, native + class projections).

## Materials and scaffolding

Code scaffold — **built and verified** (Temurin JDK 21, offline):
`code/projections_Students/` (hand-out) and `code/projections_Solution/` (reference,
not distributed). Boot 3.4.5, Java 21, H2 in-memory, package
`cat.tecnocampus.projections`, SQL logging on. `Product`(name, heavy `description`,
price); a listing needs only name + price, so reading the full entity over-fetches
`description`.

- Two projection styles sit side by side. The **interface projection**
  (`ProductNamePrice` + `findAllNamePrice`) is a **worked example** — declarative, so
  nothing to fill. The **class/DTO projection** (`ProductSummary` record +
  `findAllSummaries`) is the **red→green gap**: its JPQL constructor expression ships
  wrong (it projects `p.description` into `name`), so it returns wrong text *and* drags
  in `description`. Students fix it to `p.name`.
- A given `SqlInspector` (Hibernate `StatementInspector`) records the SQL so the test
  asserts on the **columns selected** — students do not build it.
- Success signal: `./mvnw test`. `ProjectionTest` starts with only
  `class_projection_selects_only_name_and_price` **red** (wrong names + `description`
  in the captured SQL); the full-entity and interface-projection tests are green.
  Fixing the constructor expression turns it green. `spring-boot:run` prints each read's
  SELECT list: full = `…description…, name, price`; both projections = `name, price`.
- **Peer-review rubric** (for the CL activity, not code): *(1)* over-fetch removed —
  only needed columns selected? *(2)* projection kind justified (interface for a
  read-only view, class/record when you want an immutable concrete type)? *(3)* read
  still correct? *(4)* naming and layering clean? Each criterion scored 0–2 with a
  required one-line reason.

> Build with **JDK 21** — JDK 25 can trip the Hibernate/Byte Buddy agent at test time.
> Only the class projection is a gap; an interface projection has no body to leave
> blank, so it is the worked example. (Averaged ratings were dropped from the read to
> keep the scaffold to a single table — a listing of name + price shows the over-fetch
> just as well.)

## Sequence

| Phase | Min | Grouping | Roles active | What happens |
|-------|-----|----------|--------------|--------------|
| Frame | 10 | whole class | teacher | Show the over-fetching read's SQL (`select p.*`); state the target columns; hand out the rubric they'll be judged by. |
| Build | 30 | base groups | all | Implement both projection styles; confirm via SQL that only projected columns are selected; make the provided test pass. |
| Swap + review | 25 | groups review another group's repo | review roles | Apply the rubric to the received work: score each criterion with a one-line reason; write **two stars and a wish**. |
| Return + revise | 15 | base groups | all | Read the review; make one improvement it justifies; note what you changed. |
| Debrief | 10 | whole class | spokespersons | When a full entity is still the right return; interface vs class; how projections and fetch joins (A4) both fight over-fetching from different angles. |

<!-- 10+30+25+15+10 = 90 min -->

## Deliverable

Per group: (1) both projection implementations with the SQL showing only projected
columns (test green), and (2) the **completed rubric** they wrote on another group's
work, plus a one-line note on the revision they made after receiving their own review.
Pushed to the team repo.

## Assessment

- **Formative:** the quality of the *reviews* is itself assessed — a rubric filled
  with reasons ("interface projection is right here, the read is read-only") shows
  transfer; ticks without reasons don't.
- **Evidence of learning:** the projected read selects only the needed columns, and
  the group's justification distinguishes interface (read-only view) from class DTO
  (when construction logic or immutability matters).

## Teacher notes

- **Common pitfalls:** an interface projection whose getters don't match query
  aliases (returns nulls); expecting a class projection to work without a JPQL
  constructor expression or the right constructor; projecting but still triggering a
  lazy association (a hidden N+1 inside the projection) — tie back to A4.
- **Timing risk:** the review phase compresses if Build overruns; keep Build to the
  two projections, no extras. *90 min is an estimate, not measured.*
- **Constraint check:** reads only — no concurrency mechanism, no `version`. Rich
  entities remain the write model; projections are a **read** concern and must not
  leak into how the domain mutates. JPA only.
- **Differentiation:** fast groups add a **dynamic** projection (generic
  `<T> findByCategory(String c, Class<T>)`) and show the same query serving both
  projection types.
