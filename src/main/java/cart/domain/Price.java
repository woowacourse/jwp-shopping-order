package cart.domain;

public class Price implements Money {
    private Long value;

    public Price() {
    }

    public Price(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
