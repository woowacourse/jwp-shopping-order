package cart.exception;

public class IdTypeException extends IllegalArgumentException {

    private static final String MESSAGE = "잘못된 id 형식입니다.";

    public IdTypeException() {
        super(MESSAGE);
    }
}
