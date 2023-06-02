package cart.exception;

public class IllegalUsePointException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "최소 사용 기준 포인트보다 작은 값의 포인트는 사용할 수 없습니다.";

    public IllegalUsePointException() {
        super(DEFAULT_MESSAGE);
    }
}
