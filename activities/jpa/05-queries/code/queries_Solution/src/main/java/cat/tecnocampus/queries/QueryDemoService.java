package cat.tecnocampus.queries;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryDemoService {

    private final ProductRepository products;
    private final ReviewRepository reviews;

    public QueryDemoService(ProductRepository products, ReviewRepository reviews) {
        this.products = products;
        this.reviews = reviews;
    }

    @Transactional
    public void seed() {
        Product radio = products.save(new Product("Radio", "Electronics"));
        Product tv = products.save(new Product("TV", "Electronics"));
        Product novel = products.save(new Product("Novel", "Books"));

        LocalDateTime t = LocalDateTime.of(2026, 1, 1, 12, 0);
        reviews.save(new Review("rE1", 5, t.plusMinutes(1), radio));
        reviews.save(new Review("rE2", 4, t.plusMinutes(2), tv));
        reviews.save(new Review("rE3", 3, t.plusMinutes(3), radio));
        reviews.save(new Review("rE4", 2, t.plusMinutes(4), tv));
        reviews.save(new Review("rE5", 1, t.plusMinutes(5), radio));
        reviews.save(new Review("rB1", 5, t.plusMinutes(1), novel));
        reviews.save(new Review("rB2", 4, t.plusMinutes(2), novel));
    }

    @Transactional(readOnly = true)
    public void showAll() {
        Pageable top3 = PageRequest.of(0, 3);
        print("A derived", reviews.findByProductCategoryOrderByCreatedAtDesc("Electronics", top3));
        print("B jpql   ", reviews.findRecentByCategoryJpql("Electronics", top3));
        print("C native ", reviews.findRecentByCategoryNative("Electronics", 3));
    }

    private void print(String label, List<Review> found) {
        String contents = found.stream().map(Review::getContent).collect(Collectors.joining(", "));
        System.out.printf("[%s] top-3 newest reviews in Electronics: %s%n", label, contents);
    }
}
