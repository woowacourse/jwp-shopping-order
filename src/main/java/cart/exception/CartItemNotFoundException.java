package cart.exception;

public class CartItemNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 cart item 이 있습니다.";

    public CartItemNotFoundException() {
        super(MESSAGE);
    }
}
