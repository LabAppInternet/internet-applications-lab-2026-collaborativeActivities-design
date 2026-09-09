# Code scaffolds students complete

When an activity is about *writing* code (JPA mapping, a conditional-UPDATE
concurrency fix, a REST endpoint, Spring Security config, validation), ship a
runnable Java / Spring Boot project with the target code removed, so groups
complete it. Mirror the existing pattern under `jpa/…_Students`.

## Layout

```
code/
  <topic>_Students/     # distributed to students; gaps present, does NOT pass yet
  <topic>_Solution/     # reference solution; kept out of the students' copy
```

Each is a standalone Maven project (`pom.xml`, `mvnw`, `src/main`, `src/test`),
package `cat.tecnocampus.<topic>`. Reuse the structure of the existing exercises
rather than inventing a new one.

## How to leave the gap

Two complementary signals, as in the existing exercises:

1. **Remove the target code** so the project fails until completed — e.g. the
   `Review` class ships *without* `@Entity` / `@ManyToOne`, so the app does not run
   until the student adds the mapping.
2. **Mark and describe the gap** so it is unambiguous:
   - An explicit marker at the spot: `// TODO(student): add the @ManyToOne mapping`.
   - A `README.md` stating what to add, which files may be edited, and how success
     is observed (e.g. "3 inserts and 1 select in the console log").

Scope the edit tightly: name the files students may change and tell them not to
touch the rest. Give an observable success signal — a passing test, an expected SQL
count, an HTTP response — so groups self-check without the solution.

## Respect the course constraints in the code

- **Concurrency:** only the conditional `UPDATE` deciding by affected-row count.
  Never introduce a `version` column, `@Version`, or explicit locking in a scaffold.
- **Architecture:** layered (controller → service → repository → model). Keep
  `@Transactional` in the service layer, not the controller.
- **Domain:** rich entities — put behaviour and invariants in the entity, private
  setters for fields changed only via behaviour. No anaemic getter/setter bags where
  the activity is meant to teach domain behaviour.
- **No inheritance between entities** — composition / `@Embeddable`.
- **One new framework per session** — a scaffold must not force students to meet JPA
  and Spring Security for the first time in the same activity.
- **English** identifiers, comments, and README.

## Solution hygiene

Keep `_Solution/` complete and runnable, and out of whatever students receive. If
the activity is peer-reviewed, the rubric — not the solution — is what circulates.
State in the activity's Teacher notes where the solution lives.

## Verify before returning

- The `_Students/` project builds but the relevant test/run **fails** at the gap.
- Completing the described gap makes it **pass**.
- The `_Solution/` project passes as shipped.
- Say plainly if you did not actually run the build (`./mvnw test`) — do not claim a
  scaffold compiles unless you verified it.
