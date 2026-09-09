# Activity 01 — materials

| File | For | Used in phase |
|------|-----|---------------|
| [`code-listing.md`](code-listing.md) | projected + handout | Frame → whole session |
| [`prediction-sheet.md`](prediction-sheet.md) | student **deliverable** (one per group) | Think → Pair → Four |
| [`lifecycle-diagram.md`](lifecycle-diagram.md) | student **deliverable** (one per group) | Think → Four |
| [`teacher-key.md`](teacher-key.md) | teacher only — **do not distribute** | Reveal → Consolidate |

The two deliverables are the blanks students fill; `teacher-key.md` has the filled
prediction table, the labelled lifecycle diagram, a transaction timeline, and the
debrief points. Diagrams are Mermaid, matching the Markdown-source workflow
(`tools/md2pdf.py` renders them).

The SQL behaviour in the key is **verified** against Spring Boot 3.4.5 / Hibernate 6.6
on H2 (JDK 21), not asserted from memory — see the note at the top of `teacher-key.md`.
