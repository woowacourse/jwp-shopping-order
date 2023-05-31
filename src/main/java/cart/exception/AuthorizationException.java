package cart.exception;

public class AuthorizationException extends MemberNotFoundException {

    private static final String MESSAGE = "맴버 인증에 실패했습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }
}
