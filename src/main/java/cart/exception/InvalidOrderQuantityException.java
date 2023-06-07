package cart.exception;

public class InvalidOrderQuantityException extends RuntimeException {
    public InvalidOrderQuantityException() {
        super("상품 주문 수량 정보가 유효하지 않습니다.");
    }
}
