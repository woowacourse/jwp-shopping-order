package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotFound extends OrderException {

        public NotFound() {
            super("존재하지 않은 주문 식별자값입니다.");
        }
    }

    public static class NotOwner extends OrderException {

        public NotOwner() {
            super("회원의 주문 정보가 아닙니다.");
        }
    }

    public static class OverFlowPoint extends OrderException {

        public OverFlowPoint() {
            super("주문 금액보다 더 많은 포인트를 사용할 수 없습니다.");
        }
    }
}
