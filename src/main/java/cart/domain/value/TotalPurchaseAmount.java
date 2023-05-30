package cart.domain.value;

public class TotalPurchaseAmount {

    private final int value;

    public TotalPurchaseAmount(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("총 구매금액은 음수가 될 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
