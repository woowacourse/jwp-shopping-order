package cart.exception.notfound;

public class OrderNotFoundException extends NotFoundException {

    private static final String message = "해당 주문을 찾을 수 없습니다. 입력한 주문 id: %d";

    public OrderNotFoundException() {
        super("해당 주문을 찾을 수 없습니다.");
    }

    public OrderNotFoundException(final Long orderId) {
        super(String.format(message, orderId));
    }
}
