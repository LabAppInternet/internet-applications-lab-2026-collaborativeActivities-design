# Cooperative-learning technique catalogue

English working version of the catalogue in `basePartida/010-pbl-activitats.md`
(the Catalan file is the source of truth for the pedagogy; update it there too if
the set changes). Pick by the **cognitive goal**, not by novelty. The last column
notes fit for this Spring Boot / PBL course.

## Formal cooperation structures

| Technique | What happens | Best for | Course fit |
|-----------|--------------|----------|-----------|
| **Jigsaw** | Each member becomes expert on one part and teaches the others. | Splitting a topic with independent parts (e.g. the four JPA relationship types). | Strong for JPA relationships, REST status-code families, validation constraints. Small group. |
| **Reverse Jigsaw** | Experts on the same part meet afterwards to contrast findings and surface gaps/contradictions. | Consolidating and error-checking after a jigsaw. | Pairs with the above when a topic has subtle pitfalls (lazy vs eager, cascade). |
| **Paired reading** | One reads, the other summarises/questions; roles alternate. | Dense reference text or a spec (RFC 9457, a JPA section). | Large or small group, low setup. |
| **1-2-4 (extended think-pair-share)** | Individual reflection → pair → group of four → whole class. | Surfacing prior ideas / design intuitions before instruction. | Good large-group opener for a design question. |
| **Round robin / rotating paper** | Each group writes one idea on a sheet that rotates; others extend or qualify it. | Generating and refining a shared list (edge cases, failure modes). | Good for enumerating concurrency failure modes. |

## Peer tutoring and support

| Technique | What happens | Best for | Course fit |
|-----------|--------------|----------|-----------|
| **Peer tutoring** | A student stronger on one part teaches another; fixed or rotating. | Uneven prior knowledge within a base group. | Useful when some students already grasped Spring basics better. |
| **Think–Act–Review (alternating roles)** | One does, the other supervises and gives feedback; then swap. | Hands-on coding with immediate review. | Natural for pair-programming a code scaffold. |
| **Cooperative base groups** | Stable groups for the whole project with assigned roles (coordinator, secretary, spokesperson, materials manager). | Ongoing project accountability. | The default project team structure across the 9 weeks. |

## Peer feedback and review

| Technique | What happens | Best for | Course fit |
|-----------|--------------|----------|-----------|
| **Structured peer review** | Feedback on another group's work against a rubric/checklist. | Reviewing a draft slice of the project (an endpoint, an entity, a test). | Strong once teams have code to exchange. Needs a clear rubric. |
| **Two stars and a wish** | Two positives and one improvement for another group's work. | Lightweight, positive peer feedback. | Good large-group review; low friction. |
| **Gallery walk** | Groups display intermediate work; the rest circulate leaving comments. | Comparing many groups' approaches to the same problem. | Good mid-project checkpoint (e.g. API designs). |

## Discussion and joint knowledge building

| Technique | What happens | Best for | Course fit |
|-----------|--------------|----------|-----------|
| **Academic controversy / structured debate** | Two subgroups defend opposing positions on a design decision, then seek consensus. | Genuine trade-offs with defensible sides. | Ideal for design decisions: conditional UPDATE vs "why not locking", 422 vs 400, composition vs inheritance. |
| **Collaborative concept map** | Build one map together, discussing connections. | Relating many concepts (transactions, isolation, anomalies). | Good synthesis activity for the concurrency block. |
| **Snowball** | Individual → pair → four → … → whole-class consensus. | Converging a whole class on a shared answer. | Good for agreeing on shared conventions (REST naming, error format). |

## Project management and tracking

| Technique | What happens | Best for | Course fit |
|-----------|--------------|----------|-----------|
| **Shared logbook** | Each member logs progress and doubts; peers respond. | Continuity between sessions. | Base-group tool across weeks. |
| **Team contract + peer self-assessment** | Members assess involvement and collaboration quality. | Accountability and fair contribution. | Start-of-project and checkpoints. |
| **Classroom Scrum/Kanban** | Distribute tasks and track status, agile-style adapted to class. | Coordinating project work in-session. | Recurring small-group ritual. |

## Choosing quickly

- **Learn new factual/conceptual material with separable parts** → Jigsaw (+ Reverse
  Jigsaw to error-check).
- **Weigh a design trade-off** → Academic controversy or 1-2-4.
- **Improve each other's code/artifacts** → Structured peer review or Two stars and
  a wish; Gallery walk to compare across groups.
- **Enumerate/space out a problem** (edge cases, failure modes) → Round robin.
- **Hands-on coding together** → Think–Act–Review (pair programming) over a scaffold.
- **Converge the whole class on a convention** → Snowball.
