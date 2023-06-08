package cart.exception;

public class EmptyCartItemsException extends IllegalArgumentException {

    private static final String MESSAGE = "cartItems 가 비어있습니다.";

    public EmptyCartItemsException() {
        super(MESSAGE);
    }
}
