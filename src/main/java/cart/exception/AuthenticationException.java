package cart.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("인증 오류입니다.");
    }
}
