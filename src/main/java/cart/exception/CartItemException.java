package cart.exception;

import cart.domain.cartItem.CartItem;
import cart.domain.member.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class IllegalProduct extends CartItemException {
        public IllegalProduct(final Long productId) {
            super("회원의 장바구니에 해당 상품이 존재하지 않습니다; productId=" + productId);
        }
    }

    public static class DuplicateProduct extends CartItemException {
        public DuplicateProduct(final Long productId) {
            super("회원의 장바구니에 해당 상품이 이미 존재합니다; productId=" + productId);
        }
    }

    public static class IllegalId extends CartItemException {
        public IllegalId(final Long id) {
            super("회원의 장바구니에 해당 id의 상품이 존재하지 않습니다; id=" + id);
        }
    }
}
