package cart.exception;

public class MemberNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 유저입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
