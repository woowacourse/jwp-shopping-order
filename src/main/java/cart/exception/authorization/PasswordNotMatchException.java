package cart.exception.authorization;

public class PasswordNotMatchException extends AuthenticationException {

    private static final String message = "사용자를 찾을 수 없습니다. 아이디와 비밀번호를 다시 입력하세요.";

    public PasswordNotMatchException() {
        super(message);
    }
}
