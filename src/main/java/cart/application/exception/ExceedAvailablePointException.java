package cart.application.exception;

public class ExceedAvailablePointException extends ExpectedException {

    private static final String MESSAGE = "해당 주문에서 사용할 수 있는 포인트를 초과했습니다.";

    public ExceedAvailablePointException() {
        super(MESSAGE);
    }
}
