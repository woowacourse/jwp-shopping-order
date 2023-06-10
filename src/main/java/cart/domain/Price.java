package cart.domain;

public class Price {
    private static final String UNIT = "원";

    private Integer value;

    public Price(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
