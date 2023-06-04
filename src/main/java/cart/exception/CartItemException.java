package cart.exception;

public class CartItemException extends RuntimeException {
    public CartItemException() {

    }

    public CartItemException(String message) {
        super(message);
    }

    public static class NoExist extends CartItemException {
        public NoExist(final String message) {
            super(message);
        }
    }

    public static class AlreadyExist extends CartItemException {
        public AlreadyExist(final String message) {
            super(message);
        }
    }

    public static class EmptyCart extends CartItemException {
        public EmptyCart(final String message) {
            super(message);
        }
    }

    public static class QuantityIncorrect extends CartItemException {
        public QuantityIncorrect(final String message) {
            super(message);
        }
    }

    public static class PriceIncorrect extends CartItemException {
        public PriceIncorrect(final String message) {
            super(message);
        }
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(final String message) {
            super(message);
        }
    }
}
