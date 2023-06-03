package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotEnoughPoint extends OrderException {

        public NotEnoughPoint() {
            super("사용할 포인트가 부족합니다.");
        }
    }
    public static class NotEnoughMoney extends OrderException {

        public NotEnoughMoney() {
            super("주문에 필요한 금액이 부족합니다.");
        }
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
