package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("다른 사용자의 카트에 접근할 수 없습니다.; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }
}
