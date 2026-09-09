package cat.tecnocampus.queries;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The same read — "the N most recent reviews for products in a given category,
 * newest first, with the product loaded" — expressed three ways. Method A (derived)
 * is done for you as a worked example. Complete B (JPQL) and C (native).
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // A) DERIVED (worked example): the method NAME is the query. Filters by the
    // associated product's category and orders by date; N comes from the Pageable.
    // Run it and read the SQL log to see what name-to-query translation produced.
    List<Review> findByProductCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    // B) TODO(student, group B): make this return the top-N NEWEST reviews for the
    // category, AND load each review's product in the SAME select (fetch join) so there
    // is no N+1. The starter query below filters by category but forgets both the
    // ordering and the fetch join — the test fails on the order and on the statement
    // count (it expects exactly 1). Fix the JPQL.
    @Query("select r from Review r where r.product.category = :category")
    List<Review> findRecentByCategoryJpql(@Param("category") String category, Pageable pageable);

    // C) TODO(student, group C): same result in raw SQL. The starter query filters by
    // category and limits the rows but forgets the ordering, so the wrong three come
    // back. Add the order (newest first) against the real column names
    // (reviews.created_at). Tables/columns: reviews(id, content, rating, created_at,
    // product_id), products(id, name, category).
    @Query(value = "select r.* from reviews r join products p on r.product_id = p.id "
            + "where p.category = :category limit :limit",
            nativeQuery = true)
    List<Review> findRecentByCategoryNative(@Param("category") String category, @Param("limit") int limit);
}
