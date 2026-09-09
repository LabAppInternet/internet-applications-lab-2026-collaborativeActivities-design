# The N+1 problem — see it, count it, kill it

A `Post` has many `Comment`s. This project loads posts and then reads each post's
comments — and pays for it with a burst of hidden SQL. Your job is to make the same
read happen in **one** statement.

## Run it first and watch

```bash
./mvnw spring-boot:run
```

At startup the app seeds 10 posts (3 comments each) and prints the SQL cost of the
same read done three ways. Before you change anything you will see:

```
[naive findAll()                               ] 10 posts,  30 comments, 11 SQL statement(s)
[findAllWithComments() [fetch join]            ] 10 posts,  30 comments, 11 SQL statement(s)
[findAllWithCommentsEntityGraph() [@EntityGraph]] 10 posts,  30 comments, 11 SQL statement(s)
```

Eleven statements for ten posts: **1** select for the posts + **N=10** more, one per
post, when its comments are first touched. That is the N+1 problem. Scroll up in the
log (SQL logging is on) to see the repeated `select ... from comments where post_id=?`.

## Your task

Edit **only** `PostRepository.java`. Two methods are stubbed with a plain
`select p from Post p`; make each load the comments up front in a single statement:

1. `findAllWithComments()` — use a **JPQL fetch join**. Mind duplicate parent rows
   (`distinct`, or return a `Set`).
2. `findAllWithCommentsEntityGraph()` — same result via
   `@EntityGraph(attributePaths = "comments")` (add the import).

Do not change the entities, the service, the runner, or `application.properties`, and
do **not** "fix" it by making the `@OneToMany` eager — that moves the cost onto every
read path and still N+1s other associations.

## Success signal

```bash
./mvnw test
```

You are done when `NPlusOneCountTest` is green: the naive read still costs `1 + N`,
and both your fixed methods cost exactly **1** statement. Re-run `spring-boot:run` and
watch the fetch-join and entity-graph lines drop to `1 SQL statement`.

## Notes

- Standalone kata on **H2 in-memory** for zero setup; the course project runs on
  PostgreSQL. The mapping is identical — only the datasource URL differs.
- The statement count comes from Hibernate's own `Statistics`
  (`generate_statistics=true`), not a manual tally of the log.
- Build/run with **JDK 21** (the project targets Java 21).
