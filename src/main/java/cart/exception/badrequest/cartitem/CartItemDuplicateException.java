package cart.exception.badrequest.cartitem;

import cart.exception.badrequest.BadRequestException;

public class CartItemDuplicateException extends BadRequestException {

    public CartItemDuplicateException() {
        super("중복된 장바구니 상품이 존재합니다.");
    }
}
