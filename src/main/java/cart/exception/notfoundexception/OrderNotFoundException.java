package cart.exception.notfoundexception;

public class OrderNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 주문 id 입니다. orderId = %d";

    public OrderNotFoundException(long orderId) {
        super(String.format(MESSAGE, orderId));
    }
}
