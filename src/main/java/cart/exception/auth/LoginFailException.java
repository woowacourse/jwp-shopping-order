package cart.exception.auth;

public class LoginFailException extends UnauthorizedException {

    public LoginFailException() {
        super("로그인에 실패하였습니다.");
    }
}
