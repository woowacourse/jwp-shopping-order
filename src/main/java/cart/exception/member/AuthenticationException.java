package cart.exception.member;

public class AuthenticationException extends MemberException {

    private static final String AUTHENTICATION_EXCEPTION_MESSAGE = "이메일과 비밀번호가 일치하지 않습니다.";

    public AuthenticationException() {
        super(AUTHENTICATION_EXCEPTION_MESSAGE);
    }
}
