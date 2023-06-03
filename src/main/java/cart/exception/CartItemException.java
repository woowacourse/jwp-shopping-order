package cart.exception;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("잘못된 멤버가 장바구니에 접근하고 있습니다.; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class NotFound extends CartItemException {
        public NotFound(final Long orderId) {
            super(orderId + "ID를 가진 장바구니 목록을 찾을 수 없습니다.");
        }
    }
}
