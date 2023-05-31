package cart.exception.cart;

import cart.exception.common.CartException;

public class InvalidCartItemOwnerException extends CartException {

    public InvalidCartItemOwnerException() {
        super("장바구니의 소유자가 아닙니다.");
    }
}
