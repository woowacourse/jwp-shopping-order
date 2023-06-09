package cart.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super("해당 주문내역을 찾을 수 없습니다.");
    }
}
