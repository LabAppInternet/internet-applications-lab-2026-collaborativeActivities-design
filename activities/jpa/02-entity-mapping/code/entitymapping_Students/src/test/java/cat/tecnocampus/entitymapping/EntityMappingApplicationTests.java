package cat.tecnocampus.entitymapping;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EntityMappingApplicationTests {

    @Test
    void contextLoads() {
        // Fails until Product is a managed @Entity (and Price is @Embeddable): the
        // repository cannot be created for a non-entity ("Not a managed type").
    }
}
