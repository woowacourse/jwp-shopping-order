package cart.cartitem.exception;

import cart.cartitem.domain.CartItem;
import cart.member.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(final String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(final CartItem cartItem, final Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }
}
