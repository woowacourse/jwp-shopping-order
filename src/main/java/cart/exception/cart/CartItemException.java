package cart.exception.cart;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.exception.member.MemberException;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class DuplicateCartItem extends CartItemException {
        public DuplicateCartItem() {
            super("이미 장바구니에 존재하는 상품입니다");
        }
    }
}
