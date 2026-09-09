# Mapping your first rich entity

`Product` and its `Price` are plain Java classes. Your job is to map them with JPA so
they can be stored and read back.

## Your task — edit only `Product.java` and `Price.java`

1. **Make `Product` an entity:** `@Entity`, `@Table(name = "products")`, an `@Id` with
   `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
2. **Map the value object:** make `Price` `@Embeddable` and annotate the field in
   `Product` with `@Embedded`. `Price` is held by **composition** — it must not have an
   `@Id`. Its columns end up in the `products` table.

`ProductRepository` (with the derived query `findByNameContaining`) and the runner are
given. Do not change them, the tests, or `application.properties`.

## How you know you're done

```bash
./mvnw test        # green when Product/Price are mapped
./mvnw spring-boot:run
```

Before mapping, every test errors with **`Not a managed type: ... Product`** — a
repository cannot be built for a class that is not an entity. Once mapped:

- `contextLoads` passes.
- `ProductMappingTest` passes: a product persists with a generated id, the embedded
  price round-trips, and `findByNameContaining("Radio")` returns exactly the FM Radio.

Run the app and read the SQL log: one `insert into products (...)` per product — note
the price columns are *in that table* (no second table) — and a `select ... where
name like ?` whose shape mirrors the derived method name.

## Extension (optional)

Add a second derived query of your own (e.g. `findByPriceCurrency(String currency)`)
and a domain invariant (reject a blank name in the `Product` constructor).

## Notes

- Standalone kata on **H2 in-memory**; the course project runs on PostgreSQL. Same
  mapping, different datasource URL.
- Build/run with **JDK 21** (the project targets Java 21).
