package cart.exception;

public class CartItemNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 cart item 입니다.";

    public CartItemNotFoundException() {
        super(MESSAGE);
    }

    public CartItemNotFoundException(final String message) {
        super(message);
    }
}
