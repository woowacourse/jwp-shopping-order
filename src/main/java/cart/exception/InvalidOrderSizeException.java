package cart.exception;

public class InvalidOrderSizeException extends CartException {
    public InvalidOrderSizeException() {
        super("주문은 최소 1개 이상의 상품을 주문해야 합니다.");
    }
}
