package cart.exception;

public class IllegalPointUsageException extends RuntimeException {
    public IllegalPointUsageException() {
        super("유효하지 않은 포인트 사용입니다.");
    }
}
