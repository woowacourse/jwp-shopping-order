package cart.exception;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public static class InvalidOrder extends OrderException {
        public InvalidOrder() {
            super("존재하지 않는 주문 내역입니다");
        }
    }

    public static class IllegalMember extends OrderException {
        public IllegalMember() {
            super("잘못된 요청입니다");
        }
    }
}
