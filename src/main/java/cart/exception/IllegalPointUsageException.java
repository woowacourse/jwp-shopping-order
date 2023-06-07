package cart.exception;

public class IllegalPointUsageException extends RuntimeException {
	public IllegalPointUsageException() {
		super("사용할 수 있는 포인트가 아닙니다");
	}
}
