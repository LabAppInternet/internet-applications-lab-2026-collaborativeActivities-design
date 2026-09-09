package cat.tecnocampus.entitymapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductMappingTest {

    @Autowired
    private ProductRepository products;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void persists_and_reads_back_with_generated_id_and_embedded_price() {
        Product saved = products.save(
                new Product("FM Radio", "Stereo FM radio", new Price(new BigDecimal("29.90"), "EUR")));
        entityManager.flush();
        entityManager.clear();

        Product found = products.findById(saved.getId()).orElseThrow();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("FM Radio");
        // The value object round-trips from columns on the products table.
        assertThat(found.getPrice().getAmount()).isEqualByComparingTo("29.90");
        assertThat(found.getPrice().getCurrency()).isEqualTo("EUR");
    }

    @Test
    void derived_query_finds_by_name_fragment() {
        products.save(new Product("FM Radio", "d", new Price(new BigDecimal("10.00"), "EUR")));
        products.save(new Product("Smart TV", "d", new Price(new BigDecimal("20.00"), "EUR")));
        entityManager.flush();
        entityManager.clear();

        List<Product> found = products.findByNameContaining("Radio");
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("FM Radio");
    }
}
