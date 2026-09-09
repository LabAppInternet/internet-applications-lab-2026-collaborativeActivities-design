package cat.tecnocampus.queries;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The same read — "the N most recent reviews for products in a given category,
 * newest first" — expressed three ways. Compare the SQL each one generates and who
 * wrote it: Spring Data (derived), Hibernate (JPQL), or you (native).
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // A) DERIVED (worked example): the method NAME is the query. It filters by the
    // associated product's category and orders by date; the limit (N) comes from the
    // Pageable. A derived query cannot fetch-join, so the product stays lazy.
    List<Review> findByProductCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    // B) JPQL: written against the entity model. The fetch join loads each review's
    // product in the SAME select (no N+1). N comes from the Pageable.
    @Query("select r from Review r join fetch r.product p "
            + "where p.category = :category order by r.createdAt desc")
    List<Review> findRecentByCategoryJpql(@Param("category") String category, Pageable pageable);

    // C) NATIVE: raw SQL against the real tables/columns. You own the SQL; Hibernate
    // just maps the rows back to Review.
    @Query(value = "select r.* from reviews r join products p on r.product_id = p.id "
            + "where p.category = :category order by r.created_at desc limit :limit",
            nativeQuery = true)
    List<Review> findRecentByCategoryNative(@Param("category") String category, @Param("limit") int limit);
}
