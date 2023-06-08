package cart.global.exception;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.member.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("해당 사용자의 장바구니에 담긴 상품이 아닙니다.");
        }
    }
}
