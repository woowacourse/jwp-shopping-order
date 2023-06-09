package cart.error.exception;

public class ConflictException extends RuntimeException{
	public ConflictException(final String message) {
		super(message);
	}

	public static class Monetary extends ConflictException {
		public Monetary() {
			super("주문 가격을 다시 확인해주세요.");
		}
	}
}
