package cart.exception;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(final String message) {
        super(message);
    }
}
