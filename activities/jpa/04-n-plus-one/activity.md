# The N+1 problem: see it, count it, kill it

| | |
|---|---|
| **Cooperative technique** | Round-robin (enumerate) → Think–Act–Review (measure & fix) |
| **Learning content** | *Lazy vs eager loading i les implicacions de rendiment*; *Optimització de queries / N+1*; *Queries amb JPQL i `@Query`* (fetch join) (`basePartida/aprendizajes.md`) |
| **Week / level** | Week 3 · small group · the highest step of the first half (`009`) |
| **Group setting** | Small group (30, 4 h) |
| **Duration** | 120 min |
| **Team size / roles** | Base groups of 4. Round-robin needs a **scribe** (rotating sheet); the fix phase uses **Driver** + **Navigator** (Navigator counts SQL). |

## Why this technique here

N+1 is invisible until you *count* — so the activity is built around measurement, not
explanation. The round-robin first makes the group **enumerate** every place a lazy
association could trigger a hidden query (spreading the diagnostic net wide), then
Think–Act–Review has one student fix while the other watches the statement count fall,
tying the remedy directly to the metric. This is the design's session-3 peak (`009`):
measure and correct an N+1 one step after the first mappings.

## Learning objectives

By the end, students can:
- **Reproduce** an N+1: iterate a collection of parents and touch a lazy association,
  and read the resulting `1 + N` selects in the log.
- **Measure** it: count SQL statements before and after, for a known dataset size.
- **Fix** it with a **JPQL fetch join** and, alternatively, an `@EntityGraph`, and
  explain the trade-off versus making the association eager (which fixes nothing and
  spreads the cost everywhere).

## Prerequisites

- A3 (associations mapped; owning side and default fetch understood).
- The SQL-log habit from A1–A2.

## Materials and scaffolding

Code scaffold — **built and verified** (Temurin JDK 21, Maven wrapper, offline):
`code/nplusone_Students/` (hand-out) and `code/nplusone_Solution/` (reference, not
distributed). Boot 3.4.5, Java 21, H2 in-memory, package `cat.tecnocampus.nplusone`,
SQL logging and `hibernate.generate_statistics` already on. Domain: `Post` 1–N
`Comment` (the textbook N+1 shape, matching the `@OneToMany` expert reading).

- `DemoRunner` seeds 10 posts × 3 comments and prints the SQL cost of the same read
  done three ways; before any fix it prints `11 SQL statement(s)` (1 + N) for all
  three lines.
- The **counter** is Hibernate's own `Statistics#getPrepareStatementCount()`, wired
  into `PostDemoService` and into the test — students do not build it.
- The only file students edit is `PostRepository.java`, with two marked gaps:
  - `// TODO(student): … JPQL fetch join …` on `findAllWithComments()`
  - `// TODO(student): … @EntityGraph(attributePaths = "comments") …` on
    `findAllWithCommentsEntityGraph()`
- Success signal: `./mvnw test`. `NPlusOneCountTest` asserts the naive read costs
  `1 + N` and each fixed method costs exactly **1**. In the students' copy the two
  fix-tests start **red** (`expected: 1 but was: 11`) and go green when completed; the
  `@SpringBootTest` context-load check passes throughout.
- Scope: students edit only `PostRepository`; entities, service, runner, tests, and
  `application.properties` are given and must not be changed.

> Build with **JDK 21** — the project targets Java 21, and this machine's default
> JDK 25 can trip the Hibernate/Byte Buddy agent at test time. Verified green on 21.

## Sequence

| Phase | Min | Grouping | Roles active | What happens |
|-------|-----|----------|--------------|--------------|
| Frame | 10 | whole class | teacher | Run the seeded read live; let the log scroll; ask "how many selects, and why that many?" |
| Round-robin: where does it hide? | 20 | base groups of 4 | scribe (rotating) | The sheet rotates; each member adds one *place* in a typical app where a lazy association would fire an extra query (list endpoints, `toString`, serialization, nested loops). No repeats. |
| Share + classify | 10 | whole class | spokespersons | Groups read their lists; teacher clusters them into "read a collection then touch a relation." |
| Act 1 — reproduce & measure | 20 | pairs | Driver+Navigator | Reproduce the N+1; record the statement count for N=10. |
| Act 2 — fetch join | 25 | pairs | Driver+Navigator (swap) | Add the fetch-join query; re-measure; count must fall to 1. |
| Act 3 — @EntityGraph & trade-off | 20 | pairs | Driver+Navigator | Add the `@EntityGraph` variant; discuss why "just make it eager" is not the fix. |
| Debrief | 15 | whole class | teacher | Fetch join vs entity graph vs (bad) global eager; when N+1 is acceptable; forward link to A5 (queries) and A6 (projections). |

<!-- 10+20+10+20+25+20+15 = 120 min -->

## Deliverable

The completed project with: (1) a recorded **before/after statement count** for a
fixed N, and (2) both a fetch-join and an `@EntityGraph` fix, with the provided test
passing (count == 1 after). Pushed to the team repo, with the count noted in the
commit message.

## Assessment

- **Formative:** the Navigator's live count is the signal; a pair that "fixed" it but
  still sees `1 + N` has made the association eager somewhere, not solved it.
- **Evidence of learning:** the statement count is 1 after the fetch join, and the
  pair can articulate why eager fetching is not equivalent (it pays the cost on every
  read path, and still N+1s for *other* lazy relations).

## Teacher notes

- **Common pitfalls:** "fixing" N+1 by switching the mapping to `FetchType.EAGER`
  (moves and multiplies the problem); fetch-joining a `List` and getting duplicate
  parents (needs `distinct` / `Set`); measuring with N=1 where the symptom hides —
  insist on N≥10.
- **Timing risk:** Act 2/3 slip if the counter isn't pre-wired — it is provided for
  exactly this reason. *120 min is an estimate, not measured; this is the session `008`
  most wants timed.*
- **Constraint check:** performance via query shape, **not** via a concurrency
  mechanism — no `version` column, no locking appears here. JPA only. Fetch join uses
  JPQL `@Query`, previewing A5.
- **Differentiation:** fast pairs add a second lazy relation and show that the fetch
  join fixes one but not the other, motivating `@EntityGraph` with multiple nodes.
