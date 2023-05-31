package cart.exception.cart;

import cart.exception.common.CartNotFoundException;

public class OrderNotFoundException extends CartNotFoundException {
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }
}
