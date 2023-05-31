package cart.exception;

public class NegativePointException extends IllegalArgumentException {

    private static final String MESSAGE = "포인트는 음수일 수 없습니다.";

    public NegativePointException() {
        super(MESSAGE);
    }
}
