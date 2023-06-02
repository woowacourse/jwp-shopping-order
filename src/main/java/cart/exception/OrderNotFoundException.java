package cart.exception;

public class OrderNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 order 입니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
