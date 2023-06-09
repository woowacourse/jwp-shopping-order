package cart.exception.network;

public class AuthenticationException extends NetworkException {

    @Override
    public int getErrorCode() {
        return 4001;
    }

    @Override
    public String getErrorMessage() {
        return "권한이 없는 사용자 입니다.";
    }
}
