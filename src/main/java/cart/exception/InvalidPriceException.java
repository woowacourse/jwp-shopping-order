package cart.exception;

public class InvalidPriceException extends RuntimeException {

    private static final String MESSAGE = "금액은 음수 일 수 없습니다.";

    public InvalidPriceException() {
        super(MESSAGE);
    }
}
