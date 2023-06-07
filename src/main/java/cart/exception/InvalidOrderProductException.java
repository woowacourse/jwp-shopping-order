package cart.exception;

public class InvalidOrderProductException extends RuntimeException {
    public InvalidOrderProductException() {
        super("유효하지 않은 제품 주문입니다.");
    }
}
