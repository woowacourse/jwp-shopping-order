package cart.exception.badrequest.cartitem;

import cart.exception.badrequest.BadRequestException;

public class CartItemQuantityException extends BadRequestException {

    public CartItemQuantityException(int limit, int currentQuantity) {
        super("장바구니 상품 수량은 최소 " + limit + "개부터 가능합니다. 현재 개수: " + currentQuantity);
    }
}
