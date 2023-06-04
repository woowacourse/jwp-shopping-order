package cart.application.exception;

public class CartItemNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당하는 장바구니 품목을 찾을 수 없습니다.";

    public CartItemNotFoundException() {
        super(MESSAGE);
    }
}
