package cart.domain.member;

import java.util.Objects;

public class Point {

    private static final int MINIMUM_POINT = 0;

    private int value;

    public Point(final int point) {
        validatePoint(point);
        this.value = point;
    }

    private void validatePoint(final int point) {
        if (point < MINIMUM_POINT) {
            throw new IllegalArgumentException("최소 포인트는 " + MINIMUM_POINT + "입니다.");
        }
    }

    public int getValue() {
        return value;
    }

    private boolean isAbleToUse(final int usingPoint) {
        validatePoint(usingPoint);
        return value >= usingPoint;
    }

    public void usePoint(final int usingPoint) {
        if (!isAbleToUse(usingPoint)) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        value -= usingPoint;
    }

    public void savePoint(final int savingPoint) {
        validatePoint(savingPoint);
        value += savingPoint;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point point = (Point) o;
        return value == point.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
