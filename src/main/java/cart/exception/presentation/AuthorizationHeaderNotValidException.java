package cart.exception.presentation;

public class AuthorizationHeaderNotValidException extends PresentationException {

    private static final String MESSAGE = "Authorization 헤더가 충분한 정보를 제공하고 있지 않습니다.";

    public AuthorizationHeaderNotValidException() {
        super(MESSAGE);
    }
}
