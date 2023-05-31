package cart.error.exception;

public class OrderException extends RuntimeException{

	public OrderException(final String message) {
		super(message);
	}

	public static class NotFound extends OrderException {
		public NotFound() {
			super("해당 주문이 존재하지 않습니다.");
		}
	}

	public static class BadRequest extends OrderException {
		public BadRequest(final String message) {
			super(message);
		}
	}

}
