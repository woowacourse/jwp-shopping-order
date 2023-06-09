package cart.exception;

public class AuthenticationException extends BadRequestException {

    public AuthenticationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
