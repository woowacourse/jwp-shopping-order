package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class CartItemException extends RuntimeException {
    public CartItemException(final String message) {
        super(message);
    }

    public static class IllegalMember extends CartItemException {
        public IllegalMember(final CartItem cartItem, final Member member) {
            super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
        }
    }

    public static class IllegalQuantity extends CartItemException {
        public IllegalQuantity(final int quantity, final int maxQuantity) {
            super("Illegal cart item quantity; quantity=" + quantity + ", max=" + maxQuantity);
        }
    }

    public static class IllegalId extends CartItemException {
        public IllegalId(final Long id) {
            super("Illegal cart item id; id=" + id);
        }
    }
}
