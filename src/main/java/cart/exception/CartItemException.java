package cart.exception;

import cart.domain.cart.Cart;
import cart.domain.member.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(Cart cart, Member member) {
            super("Illegal member attempts to cart; cart =" + cart.getId() + ", memberId=" + member.getId());
        }
    }
}
