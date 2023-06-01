package cart.error.exception;

public class CartItemException extends RuntimeException {
	public CartItemException(String message) {
		super(message);
	}

	public static class NotFound extends CartItemException {
		public NotFound() {
			super("카트에 해당 물품이 존재하지 않습니다.");
		}
	}

}
