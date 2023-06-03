package cart.application.exception;

public class ExceedOwnedPointException extends RuntimeException {

    private static final String MESSAGE = "보유한 포인트보다 많은 포인트를 사용할 수 없습니다.";

    public ExceedOwnedPointException() {
        super(MESSAGE);
    }
}
