package cart.exception.value.quantity;

import cart.exception.value.ValueException;

public class InvalidQuantityException extends ValueException {

    private static final String INVALID_QUANTITY_EXCEPTION_MESSAGE = "상품 수량은 음수가 될 수 없습니다.";

    public InvalidQuantityException() {
        super(INVALID_QUANTITY_EXCEPTION_MESSAGE);
    }
}
