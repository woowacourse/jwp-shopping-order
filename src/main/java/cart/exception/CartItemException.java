package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember() {
            super("잘못된 접근입니다.");
        }
    }

    public static class DuplicatedCartItem extends CartItemException {
        public DuplicatedCartItem() {
            super("이미 장바구니에 담긴 상품입니다.");
        }
    }
}
