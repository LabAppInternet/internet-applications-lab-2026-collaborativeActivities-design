package cat.tecnocampus.queries;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The same requirement, three ways. All three must return the top-3 newest reviews in
 * Electronics — rE5, rE4, rE3 — and the JPQL fetch join must load the product in one
 * statement.
 */
@DataJpaTest
class QueryStylesTest {

    @Autowired
    private ProductRepository products;
    @Autowired
    private ReviewRepository reviews;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Statistics statistics;

    @BeforeEach
    void seed() {
        statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);

        Product radio = products.save(new Product("Radio", "Electronics"));
        Product tv = products.save(new Product("TV", "Electronics"));
        Product novel = products.save(new Product("Novel", "Books"));

        LocalDateTime t = LocalDateTime.of(2026, 1, 1, 12, 0);
        reviews.save(new Review("rE1", 5, t.plusMinutes(1), radio));
        reviews.save(new Review("rE2", 4, t.plusMinutes(2), tv));
        reviews.save(new Review("rE3", 3, t.plusMinutes(3), radio));
        reviews.save(new Review("rE4", 2, t.plusMinutes(4), tv));
        reviews.save(new Review("rE5", 1, t.plusMinutes(5), radio));
        reviews.save(new Review("rB1", 5, t.plusMinutes(1), novel));  // Books — must be excluded
        reviews.save(new Review("rB2", 4, t.plusMinutes(2), novel));

        entityManager.flush();
        entityManager.clear();
    }

    private static List<String> contents(List<Review> found) {
        return found.stream().map(Review::getContent).toList();
    }

    @Test
    void derived_worked_example_returns_top3_newest_in_electronics() {
        List<Review> found = reviews.findByProductCategoryOrderByCreatedAtDesc(
                "Electronics", PageRequest.of(0, 3));
        assertThat(contents(found)).containsExactly("rE5", "rE4", "rE3");
    }

    @Test
    void jpql_fetch_join_returns_top3_and_loads_product_in_one_statement() {
        statistics.clear();
        List<Review> found = reviews.findRecentByCategoryJpql("Electronics", PageRequest.of(0, 3));
        assertThat(contents(found)).containsExactly("rE5", "rE4", "rE3");
        found.forEach(review -> review.getProduct().getName()); // product already fetched
        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
    }

    @Test
    void native_returns_top3_newest_in_electronics() {
        List<Review> found = reviews.findRecentByCategoryNative("Electronics", 3);
        assertThat(contents(found)).containsExactly("rE5", "rE4", "rE3");
    }
}
