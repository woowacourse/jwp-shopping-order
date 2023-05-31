package cart.exception.cart;

import cart.exception.common.CartNotFoundException;

public class ProductNotFoundException extends CartNotFoundException {

    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }
}
