package cat.tecnocampus.projections;

import java.math.BigDecimal;

/**
 * Class-based (DTO) projection: an immutable record built by a JPQL constructor
 * expression. Use a class projection when you want a concrete, immutable type (or
 * construction logic); use an interface projection for a plain read-only view.
 */
public record ProductSummary(String name, BigDecimal price) {
}
