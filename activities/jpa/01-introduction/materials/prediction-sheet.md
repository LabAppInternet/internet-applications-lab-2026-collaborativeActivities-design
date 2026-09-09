# Prediction sheet — deliverable

Group: ____________________   Members: ______________________________________

Fill one row per numbered line of the code listing. For each line decide: does it send
any SQL? if so, which statement (`insert` / `select` / `update`)? and *when* does it
run — immediately on that line, or later (at **commit / flush**)?

Do it **individually first**, then reconcile in your pair, then in your four. Keep any
disagreement visible — write both predictions rather than erasing one.

## The transaction (lines 1–5)

| Line | What the line does | SQL? (Y/N) | Which statement | When |
|------|--------------------|:----------:|-----------------|------|
| 1 | `new Product("FM Radio")` | | | |
| 2 | `save(product)` | | | |
| 3 | `findById(product.getId())` | | | |
| 4 | `found.setName("DAB Radio")` (no `save`) | | | |
| 5 | method returns → **commit** | | | |

**Totals — before you see the log, commit to a number:**

- inserts: ____   selects: ____   updates: ____   → total statements: ____

**Line 3 — circle one:** `found` **is** / **is not** the same object as `product`.
One sentence why, and what it implies about a `SELECT`:

_______________________________________________________________________________

## Bonus — detached (lines 6–7)

`product` is now detached (the transaction closed).

| Line | What the line does | SQL? (Y/N) | Which statement | When |
|------|--------------------|:----------:|-----------------|------|
| 6 | `product.setName("Retro Radio")` (no `save`) | | | |
| 7 | `save(product)` on the detached entity | | | |

## After the reveal

Where did your group's prediction differ from the actual log? Note the line(s) and the
reason:

_______________________________________________________________________________

_______________________________________________________________________________
