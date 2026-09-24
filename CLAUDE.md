# Project context

Teaching design for **Internet Applications Laboratory** (TecnoCampus · UPF),
6 ECTS, third year, first term, academic year 2026-2027. Teaching staff: Josep Roure
and Alfredo Rueda.

This repository contains the **design of activities using collaborative learning techniques**, not production code.

## Constraints of the project being designed

These constraints are decisions already made, not open preferences. Do not propose
alternatives that contradict them without explicitly warning that you are doing so.

- **Monolith.** A single Spring Boot service and a single PostgreSQL database.
  Microservices and distributed systems are third-term content.
- **Layered architecture.** Controller, service, repository, model. Ports and
  adapters (hexagonal) are second-term content.
- **Rich domain entities**, not an anemic model.
- **A single concurrency mechanism at the core**: the conditional `UPDATE`, deciding
  by the number of affected rows. No optimistic locking, no pessimistic locking, no
  isolation levels. There is no `version` column anywhere. See `decisions/004`.
- **No entity inheritance.** Composition. See `decisions/005`.
- **Progressive difficulty curve**: the first use cases are deliberately simple.
  See `decisions/003`.
- **Students' starting point.** They bring **basic Spring Framework** from a previous
  course —simple REST APIs, controllers, injection—. **They have never seen JPA,
  Spring Security, or concurrency applied to real Java.** This yields a design rule:
  **two new frameworks are not stacked in the same week**, and in particular JPA and
  Spring Security do not share an introductory session. See `decisions/009`.
- **The course is taught in English.** The documents are currently in Spanish so they
  can be worked on more quickly; the final version will be in English, code
  identifiers included.

## Document workflow

**Content evolves in Markdown. The HTML is frozen.**

- The source of truth is `markdown/*.md`, with diagrams in Mermaid. Any
  improvement, correction, or new content goes here. It is edited by hand or, more
  often, from the IDE with the integrated Claude Code —better in VS Code than in
  IntelliJ IDEA—: prompts are given and the Markdown file is modified.
- `disseny/*.html` is **never edited**. It is kept because it fixes the style guide
  of the material —the use-case cards, the level labels, the colors of the HTTP
  verbs, the rubric bars— and this design is highly valuable. It is a visual
  reference, not live content. The files are read-only on disk.
- `tools/html2md.py` performed the conversion once and **must not be run again**: it
  would overwrite the source of truth. It has a guard that prevents this.
- **The generator already exists**: `tools/md2pdf.py` produces HTML and PDF from the
  Markdown by applying the design system in `disseny/`. The PDFs are in `pdf/` and
  are regenerated, never edited. The same generator will be used for the student
  assignment briefs: only the input Markdown will change.

If a request involves touching a file in `disseny/`, stop and say so. The change
goes in the Markdown.

## Collaborative Learning

## Register and tone

- Engineering and academic register. No editorial or sensationalist tone.
- Precise terminology: *lost update*, *write skew*, *read committed*.
- Claims are supported with measurements, not emphasis. If a figure has not been
  measured, say so.
