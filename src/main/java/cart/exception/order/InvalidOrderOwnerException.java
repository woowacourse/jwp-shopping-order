package cart.exception.order;

import cart.exception.CartException;

public class InvalidOrderOwnerException extends CartException {
    public InvalidOrderOwnerException() {
        super("사용자의 주문이 아닙니다.");
    }
}
