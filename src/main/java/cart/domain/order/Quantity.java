package cart.domain.order;

import cart.exception.badrequest.order.OrderQuantityException;

class Quantity {

    private static final int MINIMUM_VALUE = 1;

    private final int value;

    public Quantity(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new OrderQuantityException("주문 상품 수량은 최소 " + MINIMUM_VALUE + "개부터 가능합니다. 현재 개수: " + value);
        }
    }

    public int getValue() {
        return value;
    }
}
