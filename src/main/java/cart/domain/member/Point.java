package cart.domain.member;

import cart.exception.badrequest.member.MemberPointException;

class Point {

    private static final int MINIMUM_VALUE = 0;

    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new MemberPointException("멤버 포인트는 최소 " + MINIMUM_VALUE + "원부터 가능합니다. 현재 포인트: " + value);
        }
    }

    public Point increase(int point) {
        if (point < MINIMUM_VALUE) {
            throw new MemberPointException("증가되는 포인트는 최소 " + MINIMUM_VALUE + "원부터 가능합니다. 현재 증가 포인트: " + point);
        }
        return new Point(value + point);
    }

    public Point decrease(int point) {
        if (point < MINIMUM_VALUE) {
            throw new MemberPointException("감소되는 포인트는 최소 " + MINIMUM_VALUE + "원부터 가능합니다. 현재 감소 포인트: " + point);
        }
        return new Point(value - point);
    }

    public int getValue() {
        return value;
    }
}
