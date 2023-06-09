package cart.exception;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {
        super(message);
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity(String message) {
            super(message);
        }
    }
}
