package cart.domain.vo;

public class Money {

    private static final int MONEY_LOWER_BOUND_EXCLUSIVE = 0;

    private final Integer value;

    private Money(Integer value) {
        this.value = value;
    }

    public static Money from(Integer value) {
        validateNumber(value);
        return new Money(value);
    }

    private static void validateNumber(int value) {
        if (value < MONEY_LOWER_BOUND_EXCLUSIVE) {
            throw new IllegalArgumentException("가격이 음수일 수 없습니다.");
        }
    }

    public Integer getValue() {
        return value;
    }

}
