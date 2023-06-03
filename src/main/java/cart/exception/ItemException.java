package cart.exception;

import cart.domain.Member;
import cart.domain.product.Item;

public class ItemException extends RuntimeException {
    public ItemException(String message) {
        super(message);
    }

    public static class IllegalMember extends ItemException {
        public IllegalMember(Item item, Member member) {
            super("Illegal member attempts to cart; cartItemId=" + item.getId() + ", memberId=" + member.getId());
        }
    }
}
