package cat.tecnocampus.entitymapping;

import java.math.BigDecimal;

/**
 * A value object: no identity of its own, defined by its values, held by composition
 * inside {@link Product}.
 *
 * TODO(student): mark this class @Embeddable (jakarta.persistence.Embeddable) so its
 * columns are stored in the owner's (products) table. This is composition, not entity
 * inheritance — do NOT give it an @Id.
 */
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
