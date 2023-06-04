package cart.domain;

import java.util.Objects;

public final class PointVO {

    private int value;

    public PointVO(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("포인트는 음수가 될 수 없습니다.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointVO)) return false;
        PointVO pointVO = (PointVO) o;
        return value == pointVO.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
