package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {

    public CartItemException(String message) {

        super(message);
    }

    public static class InvalidMember extends CartItemException {

        public InvalidMember(CartItem cartItem, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }

        public InvalidMember(String message) {
            super(message);
        }
    }

    public static class CartItemNotExists extends CartItemException {

        public CartItemNotExists(String message) {
            super(message);
        }
    }

    public static class NotFound extends CartItemException {

        public NotFound(String message) {
            super(message);
        }
    }

    public static class InvalidId extends CartItemException {

        public InvalidId(String message) {
            super(message);
        }
    }

    public static class InvalidQuantity extends CartItemException {

        public InvalidQuantity(String message) {
            super(message);
        }
    }

    public static class InvalidProduct extends CartItemException {

        public InvalidProduct(String message) {
            super(message);
        }
    }
}
