package cart.exception;

public class AuthorizationException extends BadRequestException {

    public AuthorizationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
