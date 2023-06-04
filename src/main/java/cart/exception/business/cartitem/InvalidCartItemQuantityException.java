package cart.exception.business.cartitem;

import cart.exception.business.BusinessException;

public class InvalidCartItemQuantityException extends BusinessException {

    private static final String MESSAGE = "장바구니 상품의 수량은 음수값이 될 수 없습니다. 입력한 수량 : %d";

    public InvalidCartItemQuantityException(final int inputId) {
        super(String.format(MESSAGE, inputId));
    }
}
