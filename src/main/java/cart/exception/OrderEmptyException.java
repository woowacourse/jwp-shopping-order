package cart.exception;

public class OrderEmptyException extends OrderException {

    public OrderEmptyException() {
        super("최소 하나 이상의 상품이 포함되어야 합니다.");
    }
}
