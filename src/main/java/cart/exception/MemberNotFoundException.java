package cart.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "회원을 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
