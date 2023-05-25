package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class AlreadyExist extends CartItemException {
        public AlreadyExist(Member member, Long productId) {
            super(member.getEmail() + "의 장바구니에 이미 " + productId + "상품이 존재합니다.");
        }
    }
}
