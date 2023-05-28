package cart.exception;

import cart.domain.CartItem;
import member.domain.Member;

public class IllegalMember extends CartItemException {

    public IllegalMember(CartItem cartItem, Member member) {
        super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
    }
}
