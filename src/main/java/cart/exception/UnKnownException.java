package cart.exception;

public class UnKnownException extends RuntimeException {
    public UnKnownException() {
        super("알 수 없는 에러가 발생하였습니다.");
    }
}
