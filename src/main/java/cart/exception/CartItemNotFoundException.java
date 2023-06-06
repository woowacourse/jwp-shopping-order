package cart.exception;

public class CartItemNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "cart item 이 존재하지 않습니다.";

    public CartItemNotFoundException() {
        super(MESSAGE);
    }

    public CartItemNotFoundException(final String message) {
        super(message);
    }
}
