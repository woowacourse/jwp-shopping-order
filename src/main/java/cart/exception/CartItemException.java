package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.http.HttpStatus;

public class CartItemException extends CustomException {

    public CartItemException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super(HttpStatus.FORBIDDEN,
                    "Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }
}
