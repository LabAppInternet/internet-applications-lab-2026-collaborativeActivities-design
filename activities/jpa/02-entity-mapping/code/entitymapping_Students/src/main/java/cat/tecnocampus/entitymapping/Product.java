package cat.tecnocampus.entitymapping;

// TODO(student): make Product a JPA entity. Add:
//   - @Entity and @Table(name = "products") on the class
//   - @Id and @GeneratedValue(strategy = GenerationType.IDENTITY) on the id
//   - @Embedded on the price field (and make Price @Embeddable — see Price.java)
// Until Product is a managed entity, the ProductRepository cannot be built and every
// test fails to load the application context ("Not a managed type: ... Product").
// Imports you will need: jakarta.persistence.*
public class Product {

    private Long id;

    private String name;
    private String description;

    private Price price;

    protected Product() { // required by JPA
    }

    public Product(String name, String description, Price price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}
