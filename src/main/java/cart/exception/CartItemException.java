package cart.exception;

import cart.domain.CartItem;

import static cart.domain.CartItem.MINIMUM_QUANTITY;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {

        super(message);
    }

    public static class InvalidMember extends CartItemException {

        public InvalidMember() {
            super("장바구니에 해당하는 회원이 존재하지 않습니다.");
        }
    }

    public static class CartItemNotExists extends CartItemException {

        public CartItemNotExists() {
            super("존재하지 않는 장바구니 상품입니다.");
        }
    }

    public static class NotFound extends CartItemException {

        public NotFound() {
            super("카트에 추가하려는 상품이 존재하지 않습니다.");
        }
    }

    public static class InvalidIdByNull extends CartItemException {

        public InvalidIdByNull() {
            super("장바구니 아이디를 입력해야 합니다.");
        }
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity(CartItem cartItem) {
            super("장바구니에 담긴 상품의 개수는 최소 " + MINIMUM_QUANTITY + " 이상이어야 합니다.; cartItemId=" + cartItem.getId() + "cartItemQuantity=" + cartItem.getQuantity());
        }
    }

    public static class InvalidProduct extends CartItemException {

        public InvalidProduct() {
            super("장바구니에 담으려는 상품이 존재하지 않습니다.");
        }
    }
}
