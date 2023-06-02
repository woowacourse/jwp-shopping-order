package cart.exception;

import cart.domain.CartItem;

import static cart.domain.CartItem.MINIMUM_QUANTITY;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {

        super(message);
    }

    public static class InvalidMember extends CartItemException {

        public InvalidMember(Long memberId) {
            super("해당 회원의 장바구니가 아닙니다. " +
                    "memberId : " + memberId);
        }
    }

    public static class NotFound extends CartItemException {

        public NotFound(Long cartId) {
            super("존재하지 않는 장바구니입니다. " +
                    "cartId : " + cartId);
        }
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity(CartItem cartItem) {
            super("장바구니에 담긴 상품의 개수는 최소 " + MINIMUM_QUANTITY + " 이상이어야 합니다. " +
                    "cartItemId=" + cartItem.getId() +
                    "cartItemQuantity=" + cartItem.getQuantity());
        }
    }

    public static class InvalidProduct extends CartItemException {

        public InvalidProduct(Long productId) {
            super("장바구니에 담으려는 상품이 존재하지 않습니다. " +
                    "productId : " + productId);
        }
    }
}
