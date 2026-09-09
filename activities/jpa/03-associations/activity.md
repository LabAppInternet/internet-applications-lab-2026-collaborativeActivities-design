# Mapping associations: four relationships, four experts

| | |
|---|---|
| **Cooperative technique** | Jigsaw, with a Reverse-Jigsaw checkpoint |
| **Learning content** | *Relacions: `@OneToMany`, `@ManyToOne`, `@ManyToMany` amb direccionament i propietari* (and `@OneToOne`); *Lazy vs eager loading* (introduced, exploited in A4) (`basePartida/aprendizajes.md`) |
| **Week / level** | Week 3 · small group |
| **Group setting** | Small group (30, 4 h) |
| **Duration** | 150 min |
| **Team size / roles** | Base groups of 4. Each member is the **expert** for one relationship type. Experts of the same type meet across groups first, then teach their home group. |

## Why this technique here

The four association types are **separable and comparable** — each has its own owning
side, default fetch, and best practice, but they rhyme. Jigsaw turns "cover four
mappings" into "each student teaches one," which forces the depth that reading alone
doesn't, and the Reverse-Jigsaw checkpoint catches the classic cross-cutting errors
(wrong owning side, missing `mappedBy`) by having same-type experts reconcile before
they teach.

## Learning objectives

By the end, each student can:
- Map their assigned relationship correctly, naming the **owning side**, the
  **direction**, and the **default fetch** (eager for `…ToOne`, lazy for `…ToMany`).
- Explain the best practice for their type: `@ManyToOne` as the natural owner of a
  one-to-many; `add/remove` helpers and `mappedBy` for a bidirectional `@OneToMany`;
  `Set` and owning-side choice for `@ManyToMany`; the shared-PK `@MapsId` form for
  `@OneToOne`.
- Predict how many tables and how many statements their mapping produces (verified
  against the SQL log).

## Prerequisites

- A2 (a single entity maps and reads correctly; the SQL-log habit).
- Expert pre-reading, assigned per role (out of class, ~30 min each):
  - `@ManyToOne` → guide `ManyToOneExpertJPA`.
  - `@OneToMany` → guide `OneToManyExpertJPA`.
  - `@ManyToMany` and `@OneToOne` → guide `OneToOneManyToManyExpertJPA`.

## Materials and scaffolding

Reuse and improve the **existing** scaffolds under `jpa/` — they already compile and
run on the seed (Boot 3.4.5, H2, SQL logging on):

- `@ManyToOne` → `jpa/…manytoone…_Students` (Product/Review; add the `@ManyToOne`).
- `@OneToMany` → `jpa/…onetomany…_Students`.
- `@ManyToMany` + `@OneToOne` → `jpa/…onetoone_manytomany…_Students` (Product,
  Category, ProductDetails, with a `ProductService`).

**Improvements to apply before running (small, verifiable):**
- In the `@ManyToOne` "Students" copy, `Product`/`Review` ship with **no** `@Entity`/
  `@Id` either, yet the README says only "add the `@ManyToOne`." Update the README so
  the gap is stated in full, or restore `@Entity`/`@Id` so the sole gap is the
  association. Keep the "3 inserts + 1 select" success signal.
- Add an explicit marker at each gap: `// TODO(student): add the @ManyToOne mapping`
  (and equivalents), matching the pattern in `templates/code-scaffold.md`.
- Add a one-line success signal to each README: the exact expected table count and
  statement count (e.g. "@ManyToMany → 3 tables: products, categories, join table").
- Keep a `_Solution/` copy of each out of the students' hand-out.

Each expert also fills an **expert one-pager** (template provided): owning side,
direction, default fetch, tables created, statements to add N children, one best
practice, one trap.

## Sequence

| Phase | Min | Grouping | Roles active | What happens |
|-------|-----|----------|--------------|--------------|
| Setup | 10 | whole class | teacher | Assign the four roles per base group; hand out the matching scaffold + guide. |
| Expert groups | 40 | same-type experts across groups | experts | Same-type experts complete their scaffold together, confirm the SQL, and draft the shared one-pager. |
| Reverse-Jigsaw check | 15 | same-type experts | experts | Experts of each type reconcile disagreements (owning side, `mappedBy`, `Set` vs `List`) into one agreed one-pager. |
| Teach home group | 60 | base groups of 4 | each expert in turn | Back home, each expert walks their group through their mapping and one-pager (≈15 min each); the group runs each scaffold and checks the SQL count. |
| Synthesis | 15 | base groups | secretary | The group fills a **comparison grid** (rows: the four types; columns: owning side, default fetch, #tables, best practice). |
| Debrief | 10 | whole class | teacher | Cross-type pattern: `…ToOne` owns and fetches eagerly by default; `…ToMany` is lazy; a bidirectional link needs one owner and helper methods. Sets up A4. |

<!-- 10+40+15+60+15+10 = 150 min -->

## Deliverable

Per base group: the **four working scaffolds** (each producing the expected table and
statement counts) and the filled **comparison grid**; per student: their expert
one-pager. Pushed to the team repo.

## Assessment

- **Formative:** listen at the "Teach" phase — an expert who can state *why* their
  side owns the relationship has understood it; one who only recites annotations has
  not. The comparison grid exposes gaps across types.
- **Evidence of learning:** each scaffold produces the predicted number of tables and
  statements, and the group can say which side to put the foreign key on and why.

## Teacher notes

- **Common pitfalls:** a unidirectional `@OneToMany` without `@JoinColumn` silently
  creating a **join table** (the `OneToManyExpert` guide's first question); a
  bidirectional `@OneToMany` with no `add/remove` helpers leaving the owning FK null;
  `@ManyToMany` on a `List` producing redundant delete+insert on update (prefer
  `Set`); a bidirectional `@OneToOne` when a shared-PK unidirectional would do.
- **Timing risk:** the "Teach" phase is 4×15 min and slips easily; a visible timer
  per expert helps. *150 min is an estimate, not measured.*
- **Constraint check:** associations only, no inheritance (composition already in
  A2), no concurrency mechanism, JPA still the only new framework. Lazy/eager is
  *named* here and *exploited* in A4 — do not pre-teach fetch joins yet.
- **Contingencies:** with three present instead of four, the `@ManyToMany` expert
  also carries `@OneToOne` (same source exercise). Do not drop `@ManyToOne` — A4
  depends on it.
- **Differentiation:** fast experts add the missing `add/remove` helpers to the
  bidirectional case and show the change in statement count.
