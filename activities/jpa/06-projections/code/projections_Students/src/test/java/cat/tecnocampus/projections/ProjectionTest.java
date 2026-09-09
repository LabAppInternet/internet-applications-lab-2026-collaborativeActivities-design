package cat.tecnocampus.projections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A projection selects only the columns it needs. The full-entity read pulls the heavy
 * 'description' column; both projections must not.
 */
@DataJpaTest
class ProjectionTest {

    @Autowired
    private ProductRepository products;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void seed() {
        products.save(new Product("Radio", "A".repeat(1000), new BigDecimal("29.90")));
        products.save(new Product("TV", "B".repeat(1000), new BigDecimal("499.00")));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void full_entity_read_over_fetches_the_description() {
        SqlInspector.clear();
        products.findAll();
        assertThat(SqlInspector.captured()).contains("description");
    }

    @Test
    void interface_projection_selects_only_name_and_price() {
        SqlInspector.clear();
        List<ProductNamePrice> rows = products.findAllNamePrice();

        assertThat(rows).extracting(ProductNamePrice::getName)
                .containsExactlyInAnyOrder("Radio", "TV");
        assertThat(SqlInspector.captured()).doesNotContain("description");
    }

    @Test
    void class_projection_selects_only_name_and_price() {
        SqlInspector.clear();
        List<ProductSummary> rows = products.findAllSummaries();

        assertThat(rows).extracting(ProductSummary::name)
                .containsExactlyInAnyOrder("Radio", "TV");
        assertThat(SqlInspector.captured()).doesNotContain("description");
    }
}
