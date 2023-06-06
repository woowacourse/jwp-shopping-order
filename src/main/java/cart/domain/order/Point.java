package cart.domain.order;

import cart.exception.badrequest.order.OrderPointException;

class Point {

    private static final int MINIMUM_VALUE = 0;
    private static final double REWORD_RATE = 0.1;

    private final int value;

    public Point(int value) {
        validate(value);
        this.value = value;
    }

    public static Point from(int totalPrice) {
        if (totalPrice < MINIMUM_VALUE) {
            throw new OrderPointException("음수 가격은 포인트 계산을 할 수 없습니다.");
        }
        return new Point((int) Math.ceil(totalPrice * REWORD_RATE));
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new OrderPointException("주문 포인트는 최소 " + MINIMUM_VALUE + "원부터 가능합니다. 현재 주문 포인트:" + value);
        }
    }

    public int getValue() {
        return value;
    }
}
