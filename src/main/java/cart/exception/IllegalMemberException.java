package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class IllegalMemberException extends CartItemException {
    public IllegalMemberException(final CartItem cartItem, final Member member) {
        super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
    }
}
