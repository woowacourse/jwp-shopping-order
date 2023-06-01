package cart.domain;

public class Price implements Money {
    private Integer value;

    public Price() {
    }

    public Price(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
