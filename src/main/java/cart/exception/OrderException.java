package cart.exception;

public class OrderException extends RuntimeException{

    public OrderException(String message) {
        super(message);
    }

    public static class InvalidOrder extends OrderException {
        public InvalidOrder() {
            super("존재하지 않는 주문 내역입니다");
        }
    }
}
