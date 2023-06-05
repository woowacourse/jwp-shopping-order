package cart.exception;

public class CartItemNotFoundException extends ShoppingOrderException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
