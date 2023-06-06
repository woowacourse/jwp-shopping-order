package cart.exception;

public class OrderNotExistException extends NoSuchDataExistException {
    private static final String ORDER_NAME = "주문";

    public OrderNotExistException(final Long orderId) {
        super(ORDER_NAME, orderId.toString());
    }
}
