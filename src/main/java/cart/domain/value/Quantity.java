package cart.domain.value;

public class Quantity {

    private final int value;

    public Quantity(final int value) {
        validateRange(value);
        this.value = value;
    }

    private void validateRange(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("수량은 0이하일 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
