package cart.exception;

public class CartItemException extends CustomException {
    public CartItemException(final ErrorMessage message) {
        super(message);
    }
}
