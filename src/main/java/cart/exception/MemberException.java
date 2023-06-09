package cart.exception;

public class MemberException extends CustomException {
    public MemberException(final ErrorMessage message) {
        super(message);
    }
}
