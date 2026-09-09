package cat.tecnocampus.projections;

import java.math.BigDecimal;

/**
 * Interface-based projection: a read-only view. Spring Data builds a proxy per row and
 * selects only the columns behind these getters — no {@code description}.
 */
public interface ProductNamePrice {
    String getName();
    BigDecimal getPrice();
}
