package cart.application.exception;

public class OrderNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당하는 주문을 찾을 수 없습니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
