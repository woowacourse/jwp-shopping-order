package cart.exception;

public class OrderException extends ShoppingException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotFound extends OrderException {

        public NotFound() {
            super("해당 주문이 존재하지 않습니다.");
        }
    }

    public static class IllegalMember extends OrderException {

        public IllegalMember() {
            super("해당 주문을 관리할 수 있는 멤버가 아닙니다.");
        }
    }
}
