package cart.exception;

import java.util.List;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {
        super(message);
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity(String message) {
            super(message);
        }
    }

    public static class NoSuchIds extends CartItemException {

        public NoSuchIds(List<Long> ids) {
            super("장바구니에 존재하지 않는 상품이 있습니다. carItemIds=" + ids);
        }
    }
}
