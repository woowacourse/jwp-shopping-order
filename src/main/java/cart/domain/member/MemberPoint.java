package cart.domain.member;

import cart.exception.point.NegativePointException;

import java.util.Objects;

public class MemberPoint {

    public static final double APPLICATION_RATE = 0.1;

    private final Integer point;

    public MemberPoint(final Integer point) {
        validatePoint(point);
        this.point = point;
    }

    private void validatePoint(final Integer point) {
        if (point < 0) {
            throw new NegativePointException();
        }
    }

    public MemberPoint minus(final MemberPoint point) {
        return new MemberPoint(this.point - point.point);
    }

    public MemberPoint addPointByTotalPrice(final int totalPrice) {
        return new MemberPoint(this.point + (int) Math.ceil((totalPrice * APPLICATION_RATE)));
    }

    public Integer getPoint() {
        return point;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberPoint other = (MemberPoint) o;
        return Objects.equals(point, other.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }
}
