package cart.cartitem.exception;

import cart.common.execption.BaseException;
import cart.common.execption.BaseExceptionType;

public class CartItemException extends BaseException {

    private final CartItemExceptionType cartItemExceptionType;

    public CartItemException(CartItemExceptionType cartItemExceptionType) {
        this.cartItemExceptionType = cartItemExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return cartItemExceptionType;
    }
}
