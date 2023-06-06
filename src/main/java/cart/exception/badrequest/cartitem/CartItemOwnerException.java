package cart.exception.badrequest.cartitem;

import cart.exception.badrequest.BadRequestException;

public class CartItemOwnerException extends BadRequestException {

    public CartItemOwnerException() {
        super("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
    }
}
