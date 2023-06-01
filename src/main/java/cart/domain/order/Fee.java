package cart.domain.order;

import cart.exception.badrequest.order.OrderDeliveryFeeException;

class Fee {

    private static final int MINIMUM_VALUE = 0;
    private static final int BASIC_DELIVERY_FEE = 3000;
    private static final int DELIVERY_FEE_FREE_PRICE = 50000;

    private final int value;

    private Fee(int value) {
        validate(value);
        this.value = value;
    }

    public static Fee from(int totalPrice) {
        if (totalPrice >= DELIVERY_FEE_FREE_PRICE) {
            return new Fee(0);
        }
        return new Fee(BASIC_DELIVERY_FEE);
    }

    private void validate(int value) {
        if (value < MINIMUM_VALUE) {
            throw new OrderDeliveryFeeException("배송료는 음수가 될 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
