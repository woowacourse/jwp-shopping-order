package cart.exception.presentation;

public class NoProperStatusCodeException extends PresentationException {

    private static final String MESSAGE = "해당 예외에 맞는 적절한 상태코드가 존재하지 않습니다.";

    public NoProperStatusCodeException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
