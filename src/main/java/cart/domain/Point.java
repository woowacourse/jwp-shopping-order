package cart.domain;

import cart.exception.IllegalUsePointException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Point {

    public static final int DEFAULT_VALUE = 0;
    public static final int MIN_USAGE_VALUE = 3_000;

    private final int value;

    public Point(final int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("포인트는 음수가 될 수 없습니다.");
        }
    }

    public static void validateUsablePoint(final int usePoint) {
        if (0 < usePoint && usePoint < MIN_USAGE_VALUE) {
            throw new IllegalUsePointException();
        }
    }
}
