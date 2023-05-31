package cart.application.exception;

import cart.application.domain.CartItem;
import cart.application.domain.Member;

public class IllegalMemberException extends CartItemException {

    private static final String MESSAGE_FORMAT = "Illegal member attempts to cart; cartItemId=%d, memberId=%d";

    public IllegalMemberException(CartItem cartItem, Member member) {
        super(String.format(MESSAGE_FORMAT, cartItem.getId(), member.getId()));
    }
}
