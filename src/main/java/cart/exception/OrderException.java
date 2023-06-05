package cart.exception;

public abstract class OrderException extends RuntimeException {

    private OrderException(String message) {
        super(message);
    }

    public static class InvalidQuantity extends OrderException {

        public InvalidQuantity(int minimumQuantity) {
            super("주문하고자 하는 상품의 수량은 " + minimumQuantity + " 이상이어야 합니다.");
        }
    }

    public static class NotFound extends OrderException {

        public NotFound() {
            super("지정한 주문을 찾을 수 없습니다.");
        }
    }

    public static class IllegalMember extends OrderException {

        public IllegalMember() {
            super("해당 사용자의 주문 내역이 아닙니다.");
        }
    }
}
