package cart.exception.business.cartitem;

import cart.exception.business.BusinessException;

import java.util.List;

public class InvalidCartItemsException extends BusinessException {

    private static final String MESSAGE = "해당 사용자의 장바구니에 없는 상품은 주문할 수 없습니다. 입력한 상품 ids : %s";

    public InvalidCartItemsException(final List<Long> inputCartItemIds) {
        super(String.format(MESSAGE, inputCartItemIds.toString()));
    }
}
