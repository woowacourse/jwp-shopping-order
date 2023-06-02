package cart.exception;

public class OrderNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "id: %s 에 해당하는 주문을 찾을 수 없습니다.";

    public OrderNotFoundException(final Long id) {
        super(String.format(MESSAGE, id));
    }
}
