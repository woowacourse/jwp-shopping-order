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
    }

    public static class IllegalQuantity extends CartItemException {
        public IllegalQuantity(int quantity, int maxQuantity) {
            super("Illegal cart item quantity; quantity=" + quantity + ", max=" + maxQuantity);
        }
    }

    public static class IllegalId extends CartItemException {
        public IllegalId(Long id) {
            super("Illegal cart item id; id=" + id);
        }
    }
}
