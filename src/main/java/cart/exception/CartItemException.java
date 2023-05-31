package cart.exception;

public class CartItemException extends ShoppingException {

    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {

        public IllegalMember() {
            super("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }
    }

    public static class QuantityShortage extends CartItemException {

        public QuantityShortage(int currentQuantity, int limit) {
            super("장바구니 상품 수량은 최소 " + limit + "개부터 가능합니다. 현재 개수: " + currentQuantity);
        }
    }

    public static class NotFound extends CartItemException {

        public NotFound() {
            super("해당 장바구니 상품이 존재하지 않습니다.");
        }
    }

    public static class DuplicateIds extends CartItemException {

        public DuplicateIds() {
            super("중복된 장바구니 상품 ID가 존재합니다.");
        }
    }
}
