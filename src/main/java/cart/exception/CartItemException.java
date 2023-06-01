package cart.exception;

import cart.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {

        public IllegalMember() {
            super("해당 사용자의 장바구니 상품이 아닙니다.");
        }
    }

    public static class AlreadyExist extends CartItemException {

        public AlreadyExist(Member member, Long productId) {
            super(member.getEmail() + "의 장바구니에 이미 " + productId + " 상품이 존재합니다.");
        }
    }

    public static class NotFound extends CartItemException {

        public NotFound() {
            super("해당 장바구니 상품을 찾을 수 없습니다.");
        }
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity() {
            super("수량은 음수일 수 없습니다.");
        }
    }
}
