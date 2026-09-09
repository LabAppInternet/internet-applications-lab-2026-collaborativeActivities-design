package cat.tecnocampus.queries;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements CommandLineRunner {

    private final QueryDemoService service;

    public DemoRunner(QueryDemoService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.seed();
        service.showAll(); // all three lines should read: rE5, rE4, rE3
    }
}
