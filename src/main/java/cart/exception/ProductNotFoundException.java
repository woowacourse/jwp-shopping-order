package cart.exception;

public class ProductNotFoundException extends ShoppingOrderException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
