package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class InvalidAmount extends OrderException {

        public InvalidAmount(String message) {
            super(message);
        }
    }

    public static class EmptyCart extends OrderException {

        public EmptyCart() {
            super("장바구니가 비어있습니다.");
        }
    }
}
