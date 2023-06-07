package cart.exception;

public class InvalidOrderCheckedException extends RuntimeException {
    public InvalidOrderCheckedException() {
        super("체크되지 않은 상품을 구매할 수 없습니다.");
    }
}
