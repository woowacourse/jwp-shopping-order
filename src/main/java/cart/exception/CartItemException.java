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

        public IllegalMember(String message) {
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

    public static class IllegalId extends CartItemException {

        public IllegalId(String message) {
            super(message);
        }
    }

    public static class IllegalQuantity extends CartItemException {

        public IllegalQuantity(String message) {
            super(message);
        }
    }

    public static class IllegalProduct extends CartItemException {

        public IllegalProduct(String message) {
            super(message);
        }
    }
}
