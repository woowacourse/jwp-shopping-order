package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {

    private final int errorCode;

    public CartItemException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super(ErrorCode.ILLEGAL_MEMBER.getErrorCode(), "Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public int getErrorCode() {
        return errorCode;
    }
}
