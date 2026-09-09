package cat.tecnocampus.entitymapping;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * A value object: it has no identity of its own, it is defined by its values and is
 * held by composition inside {@link Product}. Mapped with @Embeddable, so its columns
 * live in the owner's table — this is composition, not entity inheritance.
 */
@Embeddable
public class Price {

    private BigDecimal amount;
    private String currency;

    protected Price() { // required by JPA
    }

    public Price(BigDecimal amount, String currency) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("price amount must be >= 0");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
