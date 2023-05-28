package cart.domain.member;

import cart.exception.InvalidPointException;

import java.util.Objects;

public class MemberPoint {

    private final Integer point;

    public MemberPoint(final Integer point) {
        validatePoint(point);
        this.point = point;
    }

    private void validatePoint(final Integer point) {
        if (point < 0) {
            throw new InvalidPointException();
        }
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
