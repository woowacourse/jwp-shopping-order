package cart.exception;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;

public class CartItemException extends ShoppingException {

    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {

        public IllegalMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class QuantityShortage extends CartItemException {

        public QuantityShortage(int currentQuantity, int limit) {
            super("장바구니 상품 수량은 최소 " + limit + "개부터 가능합니다. 현재 개수: " + currentQuantity);
        }
    }
}
