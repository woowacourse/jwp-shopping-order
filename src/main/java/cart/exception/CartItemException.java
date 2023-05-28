package cart.exception;

public class CartItemException extends RuntimeException {
    public CartItemException(final String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember() {
            super("장바구니 아이템 소유자와 사용자가 다릅니다.");
        }
    }
}
