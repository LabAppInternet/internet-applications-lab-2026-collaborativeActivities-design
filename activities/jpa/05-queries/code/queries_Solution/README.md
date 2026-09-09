# Three ways to ask — reference solution

Completed version of the `05-queries` scaffold. **Do not distribute** — hand out
`queries_Students/`.

## What differs from the students' copy

Only `ReviewRepository.java`, methods B and C:

```java
// B — JPQL fetch join
@Query("select r from Review r join fetch r.product p "
        + "where p.category = :category order by r.createdAt desc")
List<Review> findRecentByCategoryJpql(@Param("category") String category, Pageable pageable);

// C — native
@Query(value = "select r.* from reviews r join products p on r.product_id = p.id "
        + "where p.category = :category order by r.created_at desc limit :limit",
        nativeQuery = true)
List<Review> findRecentByCategoryNative(@Param("category") String category, @Param("limit") int limit);
```

Method A (derived) is identical in both copies (the worked example).

## Verify

```bash
./mvnw test        # BUILD SUCCESS — 4 tests, 0 failures
./mvnw spring-boot:run   # all three lines: rE5, rE4, rE3
```

Verified green with Temurin **JDK 21**, offline.

## Teaching notes

- The derived query is the worked example because a derived query has **no body** to
  leave blank — its logic is entirely in the method name. Group A's job is to read it,
  run it, and show that it *cannot* fetch-join (hence the product stays lazy). B and C
  are the real red→green gaps.
- `join fetch` on a to-**one** with a `Pageable` limit is safe (no in-memory
  pagination warning); that warning only fires for to-**many** fetch joins.
- The fetch-join test asserts a statement count of 1 via Hibernate `Statistics`
  (`generate_statistics=true`) — the same counter as activity 04.
- The native query is the only one written against physical column names
  (`created_at`, `product_id`), which is exactly why it is the least portable —
  a good hook for the H2-vs-PostgreSQL point.
