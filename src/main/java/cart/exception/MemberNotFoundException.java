package cart.exception;

public class MemberNotFoundException extends GlobalException {

    private static final String message = "해당 사용자가 존재하지 않습니다. 입력한 이메일: %s";

    public MemberNotFoundException() {
        super("해당 사용자가 존재하지 않습니다.");
    }

    public MemberNotFoundException(final String email) {
        super(String.format(message, email));
    }
}
