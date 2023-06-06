package cart.exception.cart;

import cart.exception.CartException;

public class CartItemNotFoundException extends CartException {
    public CartItemNotFoundException() {
        super("장바구니에 담긴 상품을 찾을 수 없습니다.");
    }
}

