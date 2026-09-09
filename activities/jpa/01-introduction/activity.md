# Introduction to JPA: objects, tables, and the SQL you didn't write

| | |
|---|---|
| **Cooperative technique** | 1-2-4 (extended think-pair-share) |
| **Learning content** | *Cicle de vida de les entitats: transient, managed, detached, removed*; *Entitats de domini com a classes anotades (`@Entity`, `@Table`, `@Column`)*; *Flush i sincronització amb la base de dades dins de transaccions* (`basePartida/aprendizajes.md`) |
| **Week / level** | Week 2 · large group · first JPA contact |
| **Group setting** | Large group (60, 2 h) — participative concept session |
| **Duration** | 60 min |
| **Team size / roles** | Individual → pair → group of four. No fixed roles; each four picks a **spokesperson** for the plenary. |

## Why this technique here

The goal is to surface and correct the mental model *before* any code: students
already "know" objects and tables separately, and 1-2-4 makes each student commit a
prediction individually before the group smooths it, so misconceptions about *when*
Hibernate touches the database become visible instead of hiding behind a confident
neighbour. It fits a large, participative slot with no scaffold to build.

## Learning objectives

By the end, students can:
- Name the four entity states (transient, managed, detached, removed) and say which
  operation moves an object between them.
- Predict, for a short snippet, **how many** SQL statements Hibernate emits and
  **when** (including at transaction flush), and check the prediction against the log.
- Explain why a managed entity modified inside a transaction is written back
  **without an explicit `save`**.

## Prerequisites

- Basic Java/Spring from the prior course. No JPA assumed — this is the entry point.
- Pre-reading (out of class, ~30 min): the existing guides
  `JPAintroductionFirstActivity` and `JPAsessions` (entity-state transitions;
  persist vs merge; the "redundant save" anti-pattern).

## Materials and scaffolding

**Produced** under [`materials/`](materials/) — Markdown with Mermaid diagrams; the SQL
behaviour is **verified** against Spring Boot 3.4.5 / Hibernate 6.6 on H2 (JDK 21), not
asserted from memory.

- [`materials/code-listing.md`](materials/code-listing.md) — the one-page listing
  (projected + handout): a `@Transactional` method that creates an entity, saves it,
  reads it back by id, and modifies the managed instance — adapted from the seed
  `ManyToOneApplication` runner. No project to run; students reason about it. Includes
  the `application.properties` logging excerpt (`hibernate.SQL=DEBUG`,
  `format_sql=true` — why the SQL is visible) and a bonus detached-entity pair for fast
  groups.
- [`materials/prediction-sheet.md`](materials/prediction-sheet.md) — the **deliverable**:
  one row per line, columns "SQL? (Y/N)", "which statement", "when", plus a totals line
  and the `found == product` identity question.
- [`materials/lifecycle-diagram.md`](materials/lifecycle-diagram.md) — the **deliverable**
  blank diagram (four states, arrows unlabelled, Mermaid) with a word bank to place.
- [`materials/teacher-key.md`](materials/teacher-key.md) — teacher only: filled table,
  labelled lifecycle diagram, a transaction timeline, the `IDENTITY`-vs-`SEQUENCE` and
  auto-flush nuances, and the debrief points. **Do not distribute.**

## Sequence

| Phase | Min | Grouping | Roles active | What happens |
|-------|-----|----------|--------------|--------------|
| Frame | 5 | whole class | teacher | Pose the driving question: *"Which lines below hit the database, and when?"* Show the listing and the log format. |
| Think (1) | 10 | individual | — | Each student fills the prediction sheet and labels the lifecycle diagram alone, in pen. |
| Pair (2) | 10 | pairs | — | Pairs compare predictions, keep disagreements explicit (do not erase — annotate). |
| Four (4) | 15 | groups of 4 | spokesperson | Fours reconcile to one predicted sheet + one labelled diagram; note any line they still can't agree on. |
| Reveal | 12 | whole class | teacher + spokespersons | Run the snippet live (or show captured log). Spokespersons report where their group's prediction diverged from the actual SQL. |
| Consolidate | 8 | whole class | teacher | Name the four states on the diagram; explain flush-time writes and the redundant-save anti-pattern from the reading. |

<!-- 5+10+10+15+12+8 = 60 min -->

## Deliverable

Per group of four: (1) the completed **predicted-vs-actual SQL sheet** with
divergences marked, and (2) the **labelled lifecycle diagram**. Collected at the end
(photo or paper).

## Assessment

- **Formative:** the teacher scans divergences during "Reveal" — the *pattern* of
  wrong predictions (usually: expecting a write at modification time rather than at
  flush) tells the room what to reinforce in A2.
- **Evidence of learning:** a group whose predicted SQL matches the log on the
  modify-without-save line has the lifecycle model right.

## Teacher notes

- **Common pitfalls:** believing every setter or `save()` call issues SQL
  immediately; not distinguishing "persist a new object" from "merge a detached
  one"; missing that reads inside the same transaction may be served from the
  persistence context without a new select.
- **Timing risk:** the "Four" phase overruns if groups relitigate everything —
  timebox hard. *This 60-min split is an estimate, not measured.*
- **Constraint check:** concept-only; no framework stacked on JPA, no concurrency
  mechanism previewed. Keeps to `009` (JPA alone this week).
- **Differentiation:** fast fours get a bonus line — a second modification after the
  transaction closes (detached) — and must predict what `save` does then.
