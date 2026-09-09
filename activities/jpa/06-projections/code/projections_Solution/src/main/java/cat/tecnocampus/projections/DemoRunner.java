package cat.tecnocampus.projections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DemoRunner implements CommandLineRunner {

    private final ProductRepository products;

    public DemoRunner(ProductRepository products) {
        this.products = products;
    }

    @Override
    public void run(String... args) {
        products.save(new Product("Radio", "A".repeat(1000), new BigDecimal("29.90")));
        products.save(new Product("TV", "B".repeat(1000), new BigDecimal("499.00")));

        System.out.println("Full entities (watch the SELECT list — it includes description):");
        SqlInspector.clear();
        products.findAll();
        System.out.println("  SQL: " + SqlInspector.captured());

        System.out.println("Interface projection (name + price only):");
        SqlInspector.clear();
        products.findAllNamePrice().forEach(p -> System.out.println("  " + p.getName() + " = " + p.getPrice()));
        System.out.println("  SQL: " + SqlInspector.captured());

        System.out.println("Class/DTO projection (name + price only):");
        SqlInspector.clear();
        products.findAllSummaries().forEach(p -> System.out.println("  " + p.name() + " = " + p.price()));
        System.out.println("  SQL: " + SqlInspector.captured());
    }
}
