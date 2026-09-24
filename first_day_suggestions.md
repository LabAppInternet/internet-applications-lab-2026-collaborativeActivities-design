# First day: introducing Affora to student teams

## Context

Students arrive with:
- Basic Spring Framework (REST controllers, dependency injection) from a prior course.
- No experience with Docker, Testcontainers, JPA, or Spring Security.
- Teams of four already formed.
- No shared mental model of the project yet.

The first day spans two sessions: a **medium group** (≈2 h, up to 60 students) for
shared introduction and a **small group** (≈4 h, up to 30 students) for hands-on
work. This document structures both.

The goal is not to teach Docker or JPA on the first day. The goal is that every
team leaves with: (1) a shared picture of the domain, (2) the application running on
their machines, (3) a first successful API call, and (4) a concrete plan for the
first week.

---

## Medium-group session (≈2 h)

### 1. The central problem — lecture, 15 min

Open with the one sentence from the repository introduction:

> *Two hundred people click the same seat in the same second. Exactly one of them gets
> it. Your job for the next nine weeks is to make sure that sentence is true.*

Do not show the codebase yet. Ask the room: *What breaks if you implement this the
obvious way?* Take two or three answers. Do not correct them — the course is built
around discovering the answer empirically. The point of this moment is to plant the
question, not to answer it.

Then present the four deliverables and the grading table from `00-introduction.md` in
under five minutes. Students do not need to memorise this; they need to know it
exists and where to find it.

### 2. Document jigsaw — 35 min

Each team member reads one document silently (15 min), then teaches the other three
what they learned (20 min, ~5 min per person). Assign by seat position so there is no
negotiation.

| Position | Document | Core question to answer for the team |
| --- | --- | --- |
| A | `docs/domain-tour.md` | What are the five roles, the eight main concepts, and how do they relate? |
| B | `docs/00-introduction.md` | What are the four deliverables, the grading weights, and the one hard checkpoint? |
| C | `docs/working-method.md` | What is "naive first"? Which test level proves what? What evidence is kept? |
| D | `docs/api-conventions.md` + `docs/acceptance-suite.md` | Which endpoints are fixed? How does the suite get its test data without touching the database directly? |

After the teach-back: one question per team to the room. Collect on the board; answer
only what is genuinely blocking, defer the rest to the small group or to the
repository.

**Why jigsaw here.** The four documents together are too long to read as a group.
Jigsaw forces every student to become an authority on one piece and to communicate it,
rather than skimming everything superficially.

### 3. Domain-mapping exercise — 20 min

In teams, on paper or a shared whiteboard, draw the domain from memory using only
what the jigsaw surfaced. No documents open. The sketch should name the eight main
concepts and draw a line for each relationship. Five minutes to draw, then a quick
gallery: each team holds up their sketch; the instructor notes what varies between
teams (it will).

This is not an assessment. The goal is to surface disagreements within and between
teams before anyone writes a database column. Common mistakes at this stage — confusing
Event with Performance, or placing the seat directly on the Order — are exactly the
kind of thing the schema review at the end of Deliverable 1 is designed to catch.

### 4. Live demo — 20 min

With Docker already running on the instructor's machine:

```bash
./mvnw spring-boot:run
```

Show that the application starts PostgreSQL automatically via Docker Compose — no
manual `docker run` command, no configuration. Then show a request from `calls.http`:

```
GET http://localhost:8080/hello

GET http://localhost:8080/me
X-User-Id: <some-uuid>
X-User-Roles: PROMOTER
```

Explain the two headers briefly: *before real authentication exists, these headers tell
the application who you are. A filter reads them once per request and makes a
Principal available to your controllers — the same API you will use after real
authentication replaces the filter in Deliverable 3. Write your controllers against
`Principal`, not against the headers themselves, and you will not notice the
transition.*

Do not go deeper into security architecture. This is a tool for the next two weeks,
not a topic for today.

### 5. Testcontainers — 5 min

Show a single test running:

```bash
./mvnw test -Dtest=ExampleEntityRepositoryTest
```

Point out that a PostgreSQL container starts and stops automatically. Students do not
configure this; the project already ships `TestcontainersConfiguration`. The only
requirement on their side is that Docker is running. *Why not H2?* Because some
correctness properties — constraints, locking, concurrency — are database-specific and
H2 would hide them. This will matter from Deliverable 2 onwards.

### 6. Close the medium group — 10 min

Three things before they leave:

1. **Docker must be installed and running by the small group.** Anyone who does not
   have it yet should install it now, before the small group. Docker Desktop works;
   Colima works on macOS. The application will not start without it.
2. **The pipeline must be green on their fork by the end of the first week.** A fresh
   clone builds green; it is their job to keep it that way.
3. **The schema review is mandatory before the end of Deliverable 1.** Explain what
   to bring: a diagram, the database guarantees, and one decision-log entry per
   modelling choice.

---

## Small-group session (≈4 h)

### 1. Clone, build, run — 45 min

Each student on their own machine:

```bash
./mvnw spring-boot:run          # app + PostgreSQL
./mvnw verify                   # full build with tests
./mvnw test -Dtest=ExampleEntityRepositoryTest
```

The expected outcome: three green. A student who does not reach this by the end of
the slot needs help now, not next week. Common blockers at this stage:

| Symptom | Likely cause |
| --- | --- |
| `Cannot connect to the Docker daemon` | Docker Desktop not running |
| `Port 5432 already in use` | A local PostgreSQL is already bound to that port; stop it or change the Compose port |
| `Connection refused` on startup | Docker is running but the container has not finished starting; wait a few seconds |
| Tests fail with `Could not find a valid Docker environment` | Testcontainers cannot reach the Docker socket; on Colima, `export DOCKER_HOST=unix:///...` may be needed |

The instructor circulates. This is not a lecture moment.

### 2. First identified endpoint — 30 min

Each team opens `calls.http` in IntelliJ or VS Code and makes the `/me` request with
a fabricated UUID and a role. Then each student writes one additional request — to any
endpoint they choose or invent — that uses the fake identity headers and returns a
non-error response.

Discussion prompt afterward: *Your controller receives a `Principal`. How does it know
the caller is a PROMOTER? Where should that check live — in the controller, in the
service, or somewhere else?* Take answers; do not resolve it. This question is answered
by the use cases in Deliverable 3.

### 3. Acceptance suite — 20 min

Run with no session set:

```bash
./mvnw verify
```

Every acceptance test is skipped. That is the correct baseline. Show where the skipped
tests live (`src/test/java/.../acceptance/`) and explain the two rules:
- They never edit this package — a checksum test enforces it.
- They implement `AcceptanceDataPort` to give the suite its test data.

Then set a low session value and show a failing test:

```bash
./mvnw verify -Dacceptance.session=2
```

*This red test is the next thing to build.* That is the whole framing.

### 4. Team planning ritual — 45 min

Each team produces, in writing, before leaving:

1. **Vertical ownership.** Which use cases does each person own? Everyone owns at
   least one complete vertical — from the entity to the end-to-end test — and
   defends it in the final week.
2. **Branching and review agreement.** How do they branch? Who reviews before a
   merge? This is a written decision, not a verbal one, because the Git history is
   evidence.
3. **First decision-log entry.** What is the first real modelling choice they have
   already made or know they will have to make? It does not need to be resolved yet.
   Context and options are enough.
4. **What each person will have done before the next session.** One concrete item per
   person, not a list of aspirations.

The instructor collects these (paper or a shared doc) and reads them before the next
session. The purpose is not control; it is to surface teams that have not distributed
work or have not understood the scope before it is too late to correct.

### 5. Optional: pipeline green — remaining time

Teams that finish early set up their own fork, push a green build, and show the
instructor the passing pipeline. This is the standard the project sets from day one.

---

## Notes for the instructor

### On Docker

Do not teach Docker as a topic. The application uses it in two narrow ways — Docker
Compose starts PostgreSQL for development, and Testcontainers starts PostgreSQL for
tests — and students only need to know those two things. The framing is: *Docker is
infrastructure. It runs the database so you do not have to install and configure
PostgreSQL yourself.* If a student wants to understand Docker deeply, that is a topic
for another course.

### On Testcontainers

Students do not configure Testcontainers. The configuration is already in
`TestcontainersConfiguration` and the example test already uses it. What they need to
know: *tests start a real PostgreSQL container automatically; you do not have to think
about it beyond keeping Docker running.* The reason for real PostgreSQL, rather than
H2, becomes self-evident in Deliverable 2 when their first concurrency test has to
detect a race condition that H2 would not reproduce.

### On the fake security

The fake security is scaffolding, not a topic. The two facts students need on day one:
- Set `X-User-Id` (a UUID) and `X-User-Roles` in the request headers.
- Use `Principal` in the controller parameter list, not the headers directly.

The reason for depending on `Principal` rather than the header values directly is
worth stating once: *Deliverable 3 replaces the fake filter with a real
SecurityFilterChain. Every controller that depends on `Principal` requires one line of
change; every controller that reads the header directly requires a rewrite.* Students
who have built on `Principal` from the start will not notice the transition.

### On the documents

Students are given four documents to read on day one (the jigsaw). The other documents
(`provisional-identity.md`, `seed-data.md`, the four deliverable documents) are
introduced at the moment they are needed, not on day one. Pointing to everything on
the first day guarantees that nothing gets read.

### Timing risk

The most common failure mode of a first lab session is that teams spend the entire
time getting Docker running and never reach the jigsaw or the planning ritual. Mitigate
this by:
- Requiring Docker to be installed before the medium group session.
- Having a working environment yourself ready to pair with blocked students while the
  rest of the group works.
- Accepting that one or two teams will not finish the planning ritual in the small
  group — the deliverable deadline is not today.
