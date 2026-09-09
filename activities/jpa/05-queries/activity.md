# Three ways to ask: derived, JPQL, native

| | |
|---|---|
| **Cooperative technique** | Gallery walk |
| **Learning content** | *Query methods `findBy*`*; *Queries amb JPQL i `@Query` per a casos complexos*; native `@Query`; *Optimització de queries* (`basePartida/aprendizajes.md`) |
| **Week / level** | Week 4 · small group |
| **Group setting** | Small group (30, 4 h) |
| **Duration** | 100 min |
| **Team size / roles** | Base groups of 4, each assigned **one query style** to implement. Roles: **author** (writes), **runner** (checks SQL), **docent** (explains at the gallery), **note-taker** (records feedback received). |

## Why this technique here

The three query styles solve the same problem with different trade-offs
(portability, type-safety, expressive power, control over the SQL). A gallery walk
puts all three implementations of the **same requirement** on the wall at once, so
students *compare* rather than learn each in isolation — the comparison is the
lesson, and circulating groups leave the feedback that surfaces the trade-offs.

## Learning objectives

By the end, students can:
- Implement the same read as a **derived query**, a **JPQL `@Query`**, and a **native
  `@Query`**, and state which element translates each into SQL (Spring Data;
  Hibernate; nobody — it is passed through).
- Say when a derived query stops being viable (too many parameters, ordering,
  traversals) and when JPQL is preferable to native (portability, mapping) — and the
  narrow cases where native earns its place.
- Recognise that JPQL can **fetch-join** (link back to A4) whereas a naive derived
  query cannot.

## Prerequisites

- A3 (associations, to traverse in queries) and A4 (N+1 and the fetch join).
- Pre-reading (out of class): the existing guide `JPAderivedAndJPQLqueries`
  (derived / JPQL / native, with its question set).

## Materials and scaffolding

Code scaffold — **built and verified** (Temurin JDK 21, offline):
`code/queries_Students/` (hand-out) and `code/queries_Solution/` (reference, not
distributed). Boot 3.4.5, Java 21, H2 in-memory, package `cat.tecnocampus.queries`,
SQL logging + `generate_statistics` on. Domain: `Product`(name, category) ← lazy
`@ManyToOne` `Review`(content, rating, createdAt). One requirement, three ways:
*"the N most recent reviews for products in a given category, newest first, with the
product loaded."* Seeded so the Electronics top-3 is deterministic (`rE5, rE4, rE3`).

- `ReviewRepository` holds all three methods. **A (derived) is a worked example** —
  a derived query has no body to leave blank, so group A studies and runs it and shows
  it *cannot* fetch-join (the product stays lazy). **B (JPQL) and C (native) are the
  red→green gaps**, each shipped with a starter query that is deliberately incomplete:
  - B `findRecentByCategoryJpql` — filters by category but omits the ordering and the
    fetch join.
  - C `findRecentByCategoryNative` — filters and limits but omits the ordering.
- Success signal: `./mvnw test`. `QueryStylesTest` has one test per style. In the
  students' copy A and `contextLoads` pass; **B and C fail** — B on both wrong order
  and a statement count > 1 (it asserts the fetch join costs exactly **1**), C on the
  wrong three rows. Completing B and C turns them green; `spring-boot:run` then prints
  `rE5, rE4, rE3` on all three lines.
- Scope: students edit only `ReviewRepository`. Each group owns one style for the
  gallery walk and sketches the others.

> Build with **JDK 21** — JDK 25 can trip the Hibernate/Byte Buddy agent at test time.
> Correction vs the earlier draft: the derived query's real limitation here is the
> **fetch join**, not ordering/limit/traversal — a derived query does all three fine
> via the method name and a `Pageable`.

## Sequence

| Phase | Min | Grouping | Roles active | What happens |
|-------|-----|----------|--------------|--------------|
| Frame | 10 | whole class | teacher | State the one requirement; assign each group a style; explain the gallery format. |
| Build | 35 | base groups | author + runner | Each group implements its style, confirms the generated SQL, and prepares a poster: the code, the emitted SQL, one pro, one con. |
| Gallery walk | 25 | circulating | docent stays, rest circulate | Half the group stays to explain; the others visit the other styles' posters and leave a **sticky note**: one question or one trade-off they noticed. |
| Regroup | 15 | base groups | note-taker | Groups read the notes left on their poster and the notes they took; fill a **three-column comparison** (derived / JPQL / native × portability, type-safety, power, when to use). |
| Debrief | 15 | whole class | teacher | Resolve the derived-query wall; JPQL as the default, native as the escape hatch; connect fetch join back to A4. |

<!-- 10+35+25+15+15 = 100 min -->

## Deliverable

Per group: the fully implemented assigned style (test green) **plus** the completed
three-column comparison grid annotated with the gallery feedback. The derived-query
group additionally submits a one-line justification if their case cannot be expressed
as a derived method. Pushed to the team repo.

## Assessment

- **Formative:** the sticky-note questions reveal misconceptions in real time (e.g.
  expecting a native query to be database-portable); the docent's explanations show
  depth.
- **Evidence of learning:** the comparison grid correctly places each style —
  derived for simple reads, JPQL for anything with joins/projections, native only
  when JPQL can't express it — and the JPQL version uses a fetch join.

## Teacher notes

- **Common pitfalls:** thinking native queries are "faster" by default; forgetting
  that a native query returns rows Hibernate won't manage unless mapped; assuming a
  derived query can order or limit arbitrarily; JPQL `SELECT` referring to columns
  rather than entity fields.
- **Timing risk:** the Build phase overruns if a group tries to implement all three;
  they implement one and *sketch* the others. *100 min is an estimate, not measured.*
- **Constraint check:** JPA only; reads only — no concurrency mechanism, no `version`.
  Native SQL is shown as an escape hatch, not a default (the register: "it is unlikely
  you will need the native ones," per the source guide).
- **Differentiation:** fast groups add pagination (`Pageable`) to their style and note
  which styles support it cleanly.
