package cart.exception;

public class OrderOwnerException extends IllegalArgumentException {

    private static final String MESSAGE = "id: %s인 주문에 접근할 수 없습니다.";

    public OrderOwnerException(final Long id) {
        super(String.format(MESSAGE, id));
    }
}
