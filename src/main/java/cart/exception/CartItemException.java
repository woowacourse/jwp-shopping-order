package cart.exception;

import cart.domain.cartItem.CartItem;
import cart.domain.member.Member;
import org.springframework.http.HttpStatus;

public class CartItemException extends ApplicationException {

    public CartItemException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }
}
