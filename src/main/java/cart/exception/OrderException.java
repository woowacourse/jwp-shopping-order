package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(final String message) {
        super(message);
    }

    public static class IllegalPoint extends OrderException {
        public IllegalPoint() {
            super("가용 포인트를 초과했습니다.");
        }
    }

    public static class IllegalPayment extends OrderException {
        public IllegalPayment() {
            super("총 결제 금액이 총 상품 가격과 맞지 않습니다.");
        }
    }
}
