---
name: collaborative-activity
description: Generate collaborative/cooperative learning activities and multi-activity topic plans for the Internet Applications Lab (Spring Boot, PBL, 3rd-year software engineering). Use when the user asks to design, draft, or plan a classroom activity, a cooperative-learning exercise (jigsaw, peer review, think-pair-share, gallery walk, academic controversy…), a plan to learn one topic through several activities, or a code-completion exercise students must finish. Produces an English Markdown activity or topic-plan sheet, optionally with a Java/Spring Boot scaffold containing TODO gaps for students.
---

# Collaborative-learning activity generator

Design cooperative-learning activities for **Laboratori d'Aplicacions Internet**
(TecnoCampus · UPF), a 6-ECTS, 3rd-year software-engineering course taught in
**project-based learning (PBL)** around building one Spring Boot + PostgreSQL
monolith. Read `CLAUDE.md` and the files under `basePartida/` before generating —
they are the source of truth for the course's constraints, the learner starting
point, and the technique catalogue.

## Two modes — ask which if unclear

1. **Single activity** — one cooperative-learning activity for a given technique +
   learning topic. Use `templates/activity.md`.
2. **Topic plan** — the learning of one topic organised as a sequence of several
   activities, **possibly of different types**, that build toward mastery. Decompose
   the topic into sub-goals and match a technique to each. May span more than one
   session and both group settings. Use `templates/topic-plan.md` (each activity in
   it follows `templates/activity.md`).

If the user's request doesn't make the mode obvious, ask which one (one short
question). Everything else you should infer or default, not interrogate.

## Inputs to settle before writing

Gather these from the request; fill gaps with the defaults below and **state the
defaults you chose** in your reply rather than blocking on questions.

| Input | How to determine | Default |
|-------|------------------|---------|
| **Learning content** | Pick from `basePartida/aprendizajes.md`. Name the exact bullet(s). | Ask if genuinely absent — this one you cannot guess. |
| **CL technique** | From `references/techniques.md`. Match the technique to the cognitive goal, not by fashion. | Recommend one and say why. |
| **Group setting** | Small group (30 students, 4 h, project work + deeper cooperative structures) or large group (60 students, 2 h, lighter/participative). See `basePartida/010-pbl-activitats.md`. | Small group. |
| **Duration** | Fit within the setting. | 60–90 min (small), 20–40 min (large). |
| **Week / level** | The course runs 9 weeks; difficulty is deliberately progressive (early use cases are simple). | Infer from the content's position in `aprendizajes.md`. |
| **Code scaffold?** | Include a Java/Spring Boot project with TODO gaps when the activity is about *writing code*, not discussing/reviewing it. | Include for JPA/concurrency/security/REST implementation topics; omit for design-discussion or review activities. |

## Hard constraints (from CLAUDE.md — never silently violate)

- **Monolith**, one Spring Boot service, one PostgreSQL DB. No microservices, no
  distributed systems (those are 3rd-trimester content).
- **Layered architecture** (controller → service → repository → model). No
  hexagonal/ports-and-adapters (2nd trimester).
- **Rich domain entities**, not anaemic models. Behaviour lives in the entity.
- **One core concurrency mechanism only: the conditional `UPDATE` deciding by the
  number of affected rows.** No optimistic/pessimistic locking, no isolation-level
  tricks, **no `version` column anywhere**.
- **Composition, not entity inheritance.**
- **Progressive difficulty.** Early activities are deliberately gentle.
- **Learner starting point:** students know **basic Spring** (REST controllers,
  DI, starting a Boot app) but have **never seen JPA, Spring Security, or applied
  Java concurrency**. **Do not stack two new frameworks in the same session**; in
  particular JPA and Spring Security never share an entry session (see
  `basePartida/009`). If an activity would breach this, stop and say so.
- **Language: English**, code identifiers included. Even though the design docs are
  currently in Catalan/Castilian, generated activities ship in English.
- If a request would touch anything in `disseny/` (frozen style guide), **stop and
  say so** — the change belongs in Markdown.

## Register

Engineering and academic. No editorial or sensational tone. Precise terminology
(*lost update*, *write skew*, *read committed*). Back claims with measurement; if a
number (timing, difficulty) isn't measured, say so rather than asserting it.

## Procedure

1. Settle the inputs above; announce chosen defaults.
2. Pick the technique via `references/techniques.md` and justify the fit in one or
   two sentences (which cognitive goal it serves for this content).
3. Copy the relevant template and fill every section. Do not leave placeholder
   headings empty — remove sections that genuinely don't apply and note why.
4. If a code scaffold is warranted, follow `templates/code-scaffold.md`: mirror the
   existing `jpa/…_Students` pattern (a runnable project with clearly marked `TODO`
   gaps and a matching solution kept out of the students' copy).
5. Write the output files (see below), then give the user a short summary: the
   technique chosen, the learning content targeted, the timing, and any constraint
   you had to work around.

## Output location and naming

**Single activity** — one folder so the sheet and its optional code travel together:

```
activities/<week>-<short-slug>/
  activity.md
  code/                        # optional scaffold, only if code-completion
    …_Students/                # students' copy, with TODO gaps
    …_Solution/                # reference solution, not distributed
```

**Topic plan** — one folder for the topic, the overview beside a numbered subfolder
per activity:

```
activities/<topic-slug>/
  topic-plan.md                # arc + sub-goal/activity map
  01-<slug>/activity.md        # each activity, in order (own code/ if needed)
  02-<slug>/activity.md
  …
```

Use `<week>` like `s2` and kebab-case slugs (`s2-jigsaw-manytoone`). A topic plan may
span weeks, so name its folder by topic, not week. Confirm the destination if the
request implies an existing location. Never overwrite an existing activity without
checking.

## Quality checklist before returning

- [ ] Targets named bullet(s) from `aprendizajes.md`, not a vague topic.
- [ ] Technique fit justified against the cognitive goal.
- [ ] Topic plan (if used): the topic is decomposed into sub-goals, the activity
      types vary to match each sub-goal's cognitive demand, and the order builds.
- [ ] Timed phases add up to the stated duration; roles assigned where the
      technique needs them.
- [ ] A concrete, collectible **deliverable** and an **assessment** cue.
- [ ] No constraint violated (concurrency mechanism, no two new frameworks,
      layering, composition, English).
- [ ] Code scaffold (if any) compiles as given, with gaps that fail until
      completed, and a separate solution.
