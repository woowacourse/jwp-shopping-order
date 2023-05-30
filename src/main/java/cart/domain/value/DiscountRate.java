package cart.domain.value;

public class DiscountRate {

    private final int value;

    public DiscountRate(final int value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(final int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("허용되지 않는 할인율입니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
