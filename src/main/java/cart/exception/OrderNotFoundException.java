package cart.exception;

public class OrderNotFoundException extends ShoppingOrderException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
