package cart.domain;

import cart.exception.InvalidPointException;

import java.util.Objects;

public class Point {
    private static final String UNIT = "원";

    private final Long value;

    public Point(Long value) {
        validatePoint(value);
        this.value = value;
    }

    private void validatePoint(Long value) {
        if (value < 0) {
            throw new InvalidPointException("포인트의 값은 음수가 될 수 없습니다");
        }
    }

    public Long getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "Point{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(value, point.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
