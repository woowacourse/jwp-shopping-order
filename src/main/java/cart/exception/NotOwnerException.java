package cart.exception;

public class NotOwnerException extends IllegalArgumentException {

    private static final String MESSAGE = "주인이 아닙니다.";

    public NotOwnerException() {
        super(MESSAGE);
    }
}
