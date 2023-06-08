package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(final String message) {
        super(message);
    }

    public static class IllegalMemberException extends CartItemException {
        public IllegalMemberException(final CartItem cartItem, final Member member) {
            super("해당 유저가 접근할 수 없는 CartItem 입니다.; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class NotFoundException extends CartItemException {
        public NotFoundException(final Long cartItemId) {
            super("해당 아이디의 CartItem을 찾을 수 없습니다.; cartItemId=" + cartItemId);
        }
    }

}
