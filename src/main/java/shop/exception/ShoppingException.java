package shop.exception;

public class ShoppingException extends RuntimeException {
    public ShoppingException(String message) {
        super(message);
    }

    public static class IllegalAccessException extends ShoppingException {
        public IllegalAccessException(String message) {
            super(message);
        }
    }
}
