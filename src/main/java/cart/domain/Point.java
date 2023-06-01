package cart.domain;

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
}
