package cat.tecnocampus.nplusone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runs at startup: seeds a small dataset and prints the SQL cost of the same read
 * done three ways. Kept out of {@link NPlusOneApplication} so JPA test slices
 * (@DataJpaTest) don't try to build it.
 */
@Component
public class DemoRunner implements CommandLineRunner {

    private final PostDemoService service;

    public DemoRunner(PostDemoService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.seed(10, 3);
        service.showNaive();        // 1 + N statements
        service.showFetchJoin();    // 1 statement once findAllWithComments() is fixed
        service.showEntityGraph();  // 1 statement once the @EntityGraph is added
    }
}
