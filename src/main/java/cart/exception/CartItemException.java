package cart.exception;

import cart.domain.CartItem;
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
        public IllegalMember(CartItem cartItem, Long memberId) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + memberId);
        }
    }
}
