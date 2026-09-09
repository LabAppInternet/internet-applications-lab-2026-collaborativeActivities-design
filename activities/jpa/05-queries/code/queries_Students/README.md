# Three ways to ask: derived, JPQL, native

One requirement, three implementations:

> **The N most recent reviews for products in a given category, newest first, with the
> product loaded.**

`Review` has a `createdAt` and a lazy `@ManyToOne` `Product`; `Product` has a
`category`. The dataset seeds Electronics (Radio, TV) and Books (Novel); the answer for
Electronics, top 3, is **rE5, rE4, rE3**.

## Your task — edit only `ReviewRepository.java`

- **A — derived (worked example, done):** `findByProductCategoryOrderByCreatedAtDesc`.
  Read it and run it; note it cannot fetch-join, so the product stays lazy.
- **B — JPQL (group B):** `findRecentByCategoryJpql`. The starter query filters by
  category but forgets the **ordering** and the **fetch join**. Fix it so it returns
  the top 3 newest *and* loads each product in the same select (no N+1).
- **C — native (group C):** `findRecentByCategoryNative`. The starter query filters and
  limits but forgets the **ordering**. Add `order by` against the real columns
  (`reviews.created_at`). Tables: `reviews(id, content, rating, created_at,
  product_id)`, `products(id, name, category)`.

Do not change the entities, the service, the runner, the tests, or
`application.properties`.

## Success signal

```bash
./mvnw test
./mvnw spring-boot:run
```

`QueryStylesTest` has one test per style. Start state: **B and C are red**, A is green.

- B is red twice over: wrong order *and* a statement count > 1 (the test asserts the
  fetch join costs exactly **1** statement). Fix the JPQL and both go green.
- C is red on the wrong three rows; add the ordering and it goes green.

When done, `spring-boot:run` prints `rE5, rE4, rE3` on all three lines.

## For the gallery walk

Fill the comparison as you go: for each style note portability, type-safety, whether
it can traverse associations, whether it can fetch-join, and when you'd reach for it.
Spoiler from the reading: you will rarely need the native one.

## Notes

- Standalone kata on **H2 in-memory**; the course project runs on PostgreSQL — which is
  exactly why the native query (tied to real SQL) is the least portable of the three.
- Build/run with **JDK 21** (the project targets Java 21).
