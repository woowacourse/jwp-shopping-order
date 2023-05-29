package cart.domain;

import cart.exception.ErrorMessage;
import cart.exception.MemberException;
import java.util.Objects;

public class Point {
    private final int value;

    public Point(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < 0) {
            throw new MemberException(ErrorMessage.INVALID_POINT);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
