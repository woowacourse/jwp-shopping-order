package cart.exception;

public class CartItemsException extends RuntimeException {

    public CartItemsException(String message) {
        super(message);
    }

    public static class Price extends CartItemsException {
        public Price() {
            super("장바구니 목록 전체 금액 계산 중 오류가 발생하였습니다.");
        }
    }
}
