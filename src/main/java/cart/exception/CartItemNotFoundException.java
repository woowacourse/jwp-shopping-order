package cart.exception;

public class CartItemNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "존재하지 않는 장바구니입니다.";

    public CartItemNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CartItemNotFoundException(final String message) {
        super(message);
    }
}
