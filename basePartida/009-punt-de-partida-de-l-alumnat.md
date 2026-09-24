# 009 · The students' starting point

**Date:** 2026-09-05 · **Status:** accepted

## Context
Five years teaching the course, and Josep longer. What follows is not a hypothesis
about who we have in the classroom: it is accumulated observation by the teaching
staff, and it explains catalogue decisions that would otherwise seem arbitrary.

## What students already bring
**Basic Spring Framework**, from a previous course: starting a Spring Boot
application, writing controllers, simple REST APIs, dependency injection.

This is not little, and it shapes the design in a positive way: **the layered
structure and the controller are not new territory**. Session 1 can ask for an
endpoint that crosses the whole stack on the first day because they already know
that part.

## What they do NOT bring
- **JPA and object-relational mapping.** No previous contact. `@Entity` is seen
  for the first time in UC-01.
- **Spring Security.** No previous contact.
- **Concurrency applied to real Java.** They may have seen threads and mutual
  exclusion as theory; they have never seen a race condition on a database nor
  written a test that provokes one.

## Decision
**Two new frameworks are not stacked in the same week.** In particular, **JPA and
Spring Security do not share the opening session**.

## Rationale
The three missing blocks —persistence, security and concurrency— are each costly
on their own. The cost does not come from the number of concepts but from **how
many you must hold at the same time**: mapping, persistence session, filter chain,
token signing and expiry, all in the same afternoon, is the known recipe for
losing teams in the second week.

Five years of observation point in this direction, and so does cognitive load
theory. **Neither is a measurement made here**, and this should be stated: what we
have is experience consistent with the literature, not evidence of our own.

## Consequences
- **N0 can take the REST controller and dependency injection for granted.** It
  cannot take anything about persistence for granted.
- **Session 2 cannot carry the first JPA mapping and the filter chain at the same
  time.** This is the concrete conflict that follows from this decision, and it is
  task 2 of the meeting of 4 September.
- **Session 3 is the steepest step of the first half of the course**: it asks
  students to measure and fix an N+1 one session after the first entity mapping.
  It cannot be lowered without giving up the efficiency block, which is core.
- **The seed repository must carry a complete mapping example** that serves as a
  model, not just the configuration. This is scaffolding that reduces load without
  lowering the standard.
- **Tools that are not content stay invisible.** Flyway is the clear case: the seed
  carries the versioned schema and the initial migration already written, at N0 a
  file is copied following the example, and the workshop spends no time on it. The
  mechanism is UC-38, which is an Extension. The rule applies to any tool that is
  not the lesson of the day.
- **Timing N0 rises in priority**: it now tells us not only whether the scope fits,
  but whether two sessions are enough for a first contact with JPA.

## Pending
The **concrete mechanism** for taking Spring Security out of session 2. The
proposal is
[`propostes/001`](../propostes/001-ajornament-de-la-seguretat.md): separate the
actor's identity from authentication, so that the ownership rules and their tests
are written from session 2 and, the day Spring Security comes in, they need not be
touched. **Pending assessment between the two.**
