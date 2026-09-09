package cat.tecnocampus.projections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Baseline: inherited findAll() reads whole Product rows — including the heavy
    // 'description' column a listing does not need. That is the over-fetch.

    // Interface projection (worked example): the aliases map to the getters on
    // ProductNamePrice, so only name + price are selected. Study this one.
    @Query("select p.name as name, p.price as price from Product p")
    List<ProductNamePrice> findAllNamePrice();

    // TODO(student): this class/DTO projection selects the WRONG column into 'name' —
    // it uses the product's description instead of its name, so the read also drags in
    // the heavy 'description' column. Fix the JPQL constructor expression to select
    // p.name (keep p.price). The test class_projection_selects_only_name_and_price
    // fails until you do (wrong names, and the SQL still contains 'description').
    @Query("select new cat.tecnocampus.projections.ProductSummary(p.description, p.price) from Product p")
    List<ProductSummary> findAllSummaries();
}
