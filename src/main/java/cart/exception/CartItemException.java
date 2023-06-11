package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.http.HttpStatus;

public class CartItemException extends ShoppingCartException {
    public CartItemException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(ErrorCode errorCode, CartItem cartItem, Member member) {
            super(errorCode.getErrorMessage()
                    + " : cartId = "
                    + cartItem.getId()
                    + ", memberId = "
                    + member.getId(), HttpStatus.FORBIDDEN);
        }
    }
}
