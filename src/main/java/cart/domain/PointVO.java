package cart.domain;

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
}
