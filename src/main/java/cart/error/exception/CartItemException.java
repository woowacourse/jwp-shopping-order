package cart.error.exception;

public class CartItemException extends RuntimeException {
	public CartItemException(String message) {
		super(message);
	}

	public static class IllegalMember extends CartItemException {
		public IllegalMember(final Long memberId) {
			super("Illegal member attempts to cart; memberId=" + memberId);
		}
	}
}
