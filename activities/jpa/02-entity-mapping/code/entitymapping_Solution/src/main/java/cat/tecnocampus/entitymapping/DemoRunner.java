package cat.tecnocampus.entitymapping;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DemoRunner implements CommandLineRunner {

    private final ProductRepository products;

    public DemoRunner(ProductRepository products) {
        this.products = products;
    }

    @Override
    public void run(String... args) {
        products.save(new Product("FM Radio", "Stereo FM radio", new Price(new BigDecimal("29.90"), "EUR")));
        products.save(new Product("Smart TV", "55 inch 4K", new Price(new BigDecimal("499.00"), "EUR")));

        System.out.println("All products:");
        products.findAll().forEach(System.out::println);

        System.out.println("Search name containing 'Radio':");
        List<Product> found = products.findByNameContaining("Radio");
        found.forEach(System.out::println);
    }
}
