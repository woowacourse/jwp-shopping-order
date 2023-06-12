package cart.exception;

public class OrderProductException extends RuntimeException {

    public OrderProductException(String message) {
        super(message);
    }

    public static class NotFound extends OrderProductException {

        public NotFound() {
            super("찾으려는 주문 상품이 존재하지 않습니다.");
        }
    }
}
