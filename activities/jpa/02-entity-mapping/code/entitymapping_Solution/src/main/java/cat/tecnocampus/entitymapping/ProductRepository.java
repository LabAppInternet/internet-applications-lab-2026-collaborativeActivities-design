package cat.tecnocampus.entitymapping;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Worked example of a DERIVED query: Spring Data turns the method NAME into the
    // query. 'findByNameContaining' becomes '... where name like %?1%'. Watch the SQL
    // log to see the where-clause that this name produces.
    List<Product> findByNameContaining(String fragment);
}
