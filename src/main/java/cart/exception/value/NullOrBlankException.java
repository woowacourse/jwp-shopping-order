package cart.exception.value;

public class NullOrBlankException extends ValueException {

    private static final String NULL_OR_BLANK_EXCEPTION_MESSAGE = "는(은) null이나 빈 칸이 될 수 없습니다.";

    public NullOrBlankException(String message) {
        super(message + NULL_OR_BLANK_EXCEPTION_MESSAGE);
    }
}
