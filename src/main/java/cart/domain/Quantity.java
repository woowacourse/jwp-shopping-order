package cart.domain;

public class Quantity {
    private final Integer value;

    public Quantity(Integer value) {
        validateQuantity(value);
        this.value = value;
    }

    private void validateQuantity(Integer value) {
        if (value < 0) {
            throw new IllegalArgumentException("상품의 양은 음수가 될 수 없습니다");
        }
    }

    public Integer getValue() {
        return value;
    }
}
