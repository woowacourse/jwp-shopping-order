package cart.error.exception;

public class ForbiddenException extends RuntimeException {
	public ForbiddenException(final String message) {
		super(message);
	}

	public static class Cart extends ForbiddenException {
		public Cart() {
			super("카트에 해당 물품이 존재하지 않습니다.");
		}
	}

	public static class Order extends ForbiddenException {
		public Order() {
			super("해당 주문이 존재하지 않습니다.");
		}
	}
}
