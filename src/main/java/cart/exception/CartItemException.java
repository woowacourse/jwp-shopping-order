package cart.exception;

import org.springframework.http.HttpStatus;

import cart.domain.CartItem;
import cart.domain.Member;

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
