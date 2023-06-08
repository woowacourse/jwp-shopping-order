package cart.common.auth.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("유저 인증이 올바른지 확인해주세요.");
    }
}
