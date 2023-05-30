package cart.domain.value;

public class Price {

    private final int value;

    public Price(final int value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 0보다 작을 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
