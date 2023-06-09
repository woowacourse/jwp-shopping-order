package cart.exception;

public class MemberNotExistException extends RuntimeException {

    public MemberNotExistException(final String message) {
        super(message);
    }
}
