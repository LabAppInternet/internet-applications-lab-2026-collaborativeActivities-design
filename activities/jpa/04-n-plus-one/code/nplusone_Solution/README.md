# The N+1 problem — reference solution

Completed version of the `04-n-plus-one` scaffold. **Do not distribute to students** —
hand out `nplusone_Students/` instead. Keep this copy for the teacher and for
regenerating the students' version.

## What differs from the students' copy

Only `PostRepository.java`. The two read methods are implemented:

```java
@Query("select distinct p from Post p join fetch p.comments")
List<Post> findAllWithComments();

@EntityGraph(attributePaths = "comments")
@Query("select p from Post p")
List<Post> findAllWithCommentsEntityGraph();
```

Everything else — entities, `PostDemoService`, `DemoRunner`, the tests,
`application.properties` — is identical to the students' copy.

## Verify

```bash
./mvnw test        # BUILD SUCCESS — 4 tests, 0 failures
./mvnw spring-boot:run
```

Expected run output (10 posts, 3 comments each):

```
[naive findAll()                               ] 10 posts,  30 comments, 11 SQL statement(s)
[findAllWithComments() [fetch join]            ] 10 posts,  30 comments,  1 SQL statement(s)
[findAllWithCommentsEntityGraph() [@EntityGraph]] 10 posts,  30 comments,  1 SQL statement(s)
```

`NPlusOneCountTest` asserts the naive read costs `1 + N` and each fix costs `1`.

## Teaching notes

- **Verified** with Temurin **JDK 21** and the Maven wrapper, offline. On JDK 25 the
  Hibernate/Byte Buddy agent may complain; build with 21 (the project's target).
- The `distinct` in the fetch join collapses the duplicate parent rows an inner
  `join fetch` on a collection produces. Drop it to let students see the duplicates.
- `@EntityGraph` here is a *fetch* graph (left join), so posts with no comments are
  still returned; the inner `join fetch` would drop them. Worth a sentence in the
  debrief.
- The counter is Hibernate `Statistics#getPrepareStatementCount()`, enabled by
  `hibernate.generate_statistics=true`.
