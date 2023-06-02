package cart.cartitem.application.exception;

import cart.cartitem.CartItem;
import cart.member.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }
}
