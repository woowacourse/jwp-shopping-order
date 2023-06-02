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

    public static class NoSuchId extends OrderException {

        public NoSuchId(Long orderId) {
            super("존재하지 않는 주문입니다. orderId: " + orderId);
        }
    }
}
