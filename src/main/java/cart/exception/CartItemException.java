package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

import java.util.List;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {

        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity(String message) {
            super(message);
        }
    }

    public static class NoSuchIds extends CartItemException {

        public NoSuchIds(List<Long> ids) {
            super("장바구니에 존재하지 않는 상품이 있습니다. carItemIds=" + ids);
        }
    }
}
