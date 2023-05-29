package cart.exception;

public class OrderNotFoundException extends CartException {
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다");
    }
}
