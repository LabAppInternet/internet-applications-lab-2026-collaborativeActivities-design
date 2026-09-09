package cat.tecnocampus.projections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Baseline: inherited findAll() reads whole Product rows — including the heavy
    // 'description' column a listing does not need. That is the over-fetch.

    // Interface projection (worked example): the aliases map to the getters on
    // ProductNamePrice, so only name + price are selected.
    @Query("select p.name as name, p.price as price from Product p")
    List<ProductNamePrice> findAllNamePrice();

    // Class/DTO projection: a JPQL constructor expression selects only name + price
    // and builds the record directly.
    @Query("select new cat.tecnocampus.projections.ProductSummary(p.name, p.price) from Product p")
    List<ProductSummary> findAllSummaries();
}
