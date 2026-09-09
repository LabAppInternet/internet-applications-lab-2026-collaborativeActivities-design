# JPA & Spring Data — learning plan

| | |
|---|---|
| **Learning content** | From `basePartida/aprendizajes.md`: *Entitats de domini com a classes anotades (`@Entity`, `@Table`, `@Column`)*; *Cicle de vida de les entitats (transient, managed, detached, removed)*; *Relacions (`@OneToMany`, `@ManyToOne`, `@ManyToMany`) amb direccionament i propietari*; *Repositoris `extends JpaRepository`*; *Query methods `findBy*`*; *Queries amb JPQL i `@Query`*; *Lazy vs eager loading i implicacions de rendiment*; *Composició vs herència (`@Embeddable`)*; *Optimització de queries / N+1*; *Projeccions DTO (interfície i classe)*. |
| **Placement** | Weeks 2–5, small-group labs (30 students, 4 h) with two concept openers in large group (60 students, 2 h). JPA is the single new framework across this span — Spring Security is deliberately kept out of these sessions (`basePartida/009`). |
| **Activities** | 6 activities, of 6 different cooperative types (1-2-4, Think–Act–Review, Jigsaw, Round-robin, Gallery walk, Structured peer review). |
| **Estimated time** | ~10 h in-class across the six activities, plus expert pre-reading out of class. **These durations are estimates, not measured** — the hour cost of a JPA use case for this project is explicitly unknown (`basePartida/008`); treat the numbers as a first hypothesis for the N0 timing exercise to correct. |

## Learning arc

At the start, students know basic Spring (REST controllers, dependency injection,
running a Boot app) but have **never seen JPA** (`basePartida/009`): no `@Entity`,
no persistence context, no idea what SQL Hibernate emits. By the end they can map a
rich domain entity and its associations, read the generated SQL critically,
recognise and fix the N+1 problem, and choose the right query and projection style
for a read.

The spine is **problem-driven and dependency-ordered**:

1. **Introduction** builds the mental model (objects ↔ tables, the entity lifecycle)
   and the single habit everything else depends on: *reading the Hibernate SQL log*.
2. **Entity mapping** maps one rich entity with a value object (composition, not
   inheritance) and reads it back with basic derived queries — the first `@Entity`,
   which `009` places in session 2.
3. **Associations** is the breadth topic: the four relationship types with owning
   side and direction, taught as a jigsaw over the existing expert materials.
4. **The N+1 problem** comes *after* associations — you cannot observe N+1 without
   an association to traverse. Students measure it (count SQL) and fix it.
5. **Queries in depth** (derived recap → JPQL → native) gives the tools to express
   reads precisely; JPQL fetch joins are revisited here as one N+1 remedy.
6. **Projections** shapes read models to fetch only what a read needs, closing the
   performance thread opened by N+1.

**Ordering note (decision to confirm with the teaching team).** Your instruction —
"do not learn N+1 before associations" — is honoured: associations (A3) is a
complete topic before N+1 (A4). Decision `009`, though, pencilled N+1 as the
*session-3* peak, one session after the first mapping. Those are compatible only if
the first association is taught inside the entity session. This plan keeps
associations whole and lets N+1 land in the following session. If you prefer to hold
`009`'s calendar exactly, move the first `@ManyToOne` into A2 and reduce A3 to the
remaining three relationship types; the rest of the arc is unchanged. Flagged, not
decided unilaterally.

## Prerequisites

- Basic Spring Boot: start an app, write a REST controller, inject a bean. Assumed
  from the prior course (`basePartida/009`), not re-taught.
- The seed repository (Boot 3.4.5, Java 21) with SQL logging already enabled —
  reused from the existing `jpa/…_Students` exercises. These standalone katas use
  **H2 in-memory** for zero setup and fast SQL observation; the course project
  itself runs on **PostgreSQL**. The difference is worth one sentence in class: the
  mapping is identical, only the datasource URL changes.

## Sub-goals and activity map

Each sub-goal gets the cooperative structure that fits its cognitive demand —
acquiring separable facts (jigsaw), practising a skill (pair programming),
comparing options (gallery walk), and judging another's choice (peer review) are
different jobs (see `../../.claude/skills/collaborative-activity/references/techniques.md`).

| # | Sub-goal | Activity | Technique | Type / purpose | Setting | Duration | Deliverable | Depends on |
|---|----------|----------|-----------|----------------|---------|----------|-------------|------------|
| 1 | Grasp ORM value + the entity lifecycle; learn to read the SQL log | `01-introduction/` | 1-2-4 | acquire concept | Large, 2 h | 60 min | Predicted-vs-actual SQL sheet + lifecycle answers | — |
| 2 | Map one rich entity (+ value object); basic CRUD and derived reads | `02-entity-mapping/` | Think–Act–Review (pair programming) | apply / practise | Small, 4 h | 90 min | Runner passes with the entity mapped; 2 derived queries | 1 |
| 3 | Map the four association types with best practices | `03-associations/` | Jigsaw (+ Reverse-Jigsaw checkpoint) | acquire separable parts | Small, 4 h | 150 min | Four working mappings + one expert one-pager each | 2 |
| 4 | Observe, measure and fix the N+1 problem | `04-n-plus-one/` | Round-robin → Think–Act–Review | apply + diagnose | Small, 4 h | 120 min | SQL-count before/after + a fetch-join/`@EntityGraph` fix | 3 |
| 5 | Choose derived vs JPQL vs native for a read | `05-queries/` | Gallery walk | apply + compare | Small, 4 h | 100 min | The same read implemented three ways + comparison notes | 3, 4 |
| 6 | Shape read models with projections; stop over-fetching | `06-projections/` | Structured peer review | apply + peer-review | Small, 4 h | 90 min | Interface + class projection + a completed peer-review sheet | 5 |

## Why this mix of types

The topic spans four distinct cognitive jobs, so it uses four families of
structure. New separable material (the four association types) is a **jigsaw**, which
turns coverage into teaching. Skills that must be practised under feedback (mapping,
fixing N+1) are **pair programming (Think–Act–Review)**. A genuine choice among
options with trade-offs (derived/JPQL/native) is a **gallery walk** so groups see
alternatives side by side. Judgement — *is this the right projection?* — is a
**structured peer review** against a rubric. Repeating one technique for all six
would flatten these differences and waste the ones that fit best.

## Activities in detail

Each activity is a full sheet under its folder, following the `activity.md`
structure; open them in order.

1. `01-introduction/activity.md` — ORM, entity lifecycle, reading the SQL log.
2. `02-entity-mapping/activity.md` — first rich `@Entity` + value object + derived reads.
3. `03-associations/activity.md` — `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@OneToOne`.
4. `04-n-plus-one/activity.md` — measure and fix N+1.
5. `05-queries/activity.md` — derived, JPQL, native.
6. `06-projections/activity.md` — interface and class projections.

## Assessment across the topic

- **Formative checkpoints:** every activity yields a collectible artifact — a
  predicted-SQL sheet, a passing runner, four mappings, a before/after SQL count, a
  three-way query comparison, a peer-review sheet. These are the in-progress
  evidence the teacher reviews; none is individually graded.
- **Evidence of topic mastery:** the JPA slice of the PBL project — the student
  team's own entities, associations, and repositories — plus the acceptance tests
  that count SQL statements. The topic is learned when a team maps a new association
  and reads it back **without an unintended N+1**, and can justify the query/
  projection style they chose. That judgement, not any single activity, is the
  summative signal.

## Materials checklist

- The three existing association scaffolds under `jpa/` (ManyToOne, OneToMany,
  OneToOne+ManyToMany "Students") — reused and improved for A3; a fourth relationship
  (`@OneToOne`) is already present in the OneToOne+ManyToMany project.
- The existing expert reading guides (converted from the `jpa/*.docx` files) — reused
  as jigsaw expert materials in A3 and as pre-reading in A1, A5, A6.
- Scaffolds for A2, A4, A5, A6 (`<activity>/code/*_Students` + `*_Solution`),
  **built and verified** with `./mvnw test` on Temurin JDK 21 (offline). Each
  `*_Students` copy compiles with a red test that goes green when the gap is filled;
  each `*_Solution` is the reference. **Build with JDK 21** — the machine default
  (JDK 25) can trip the Hibernate/Byte Buddy agent at test time.
- Rubrics: one for the A6 peer review; the A3 expert one-pager template.

## Teacher notes

- **Sequencing risks:** A3 (associations) and A4 (N+1) are the load-bearing core; A5
  and A6 are polish and can compress or move to out-of-class if a week runs short.
  A1 can be cut to 30 min or folded into the start of A2 if the large-group slot is
  unavailable.
- **Constraint check across the span:** JPA is the only new framework here — Spring
  Security stays out (`009`). No entity inheritance is taught; the value object in A2
  models composition (`@Embeddable`). Nothing in this topic introduces the conditional
  `UPDATE` concurrency mechanism or a `version` column — concurrency is a later
  topic and these katas must not pre-empt it. Layering (repository → service) appears
  from A2 onward; keep `@Transactional` in the service layer.
- **The timing is a hypothesis.** Per `008`/`009`, the point of implementing a timed
  N0 is to replace these estimates with measured hours. Record actual durations the
  first time these run.
- **Contingencies:** the jigsaw (A3) assumes four roughly balanced experts per base
  group of four; with absences, collapse `@OneToOne` into the `@ManyToMany` expert's
  remit (both come from the same existing exercise).
