package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.exception.ForbiddenException;
import java.util.List;
import java.util.Objects;

public class Cart {

    private final Member member;
    private final List<CartItemWithId> cartItems;

    public Cart(final Member member, final List<CartItemWithId> cartItems) {
        this.member = member;
        this.cartItems = cartItems;
    }

    public void checkOwner(final Long cartItemId, final String memberName) {
        if (!Objects.equals(this.member.name(), memberName)) {
            throw new ForbiddenException(String.valueOf(cartItemId), memberName);
        }
    }

    public Member getMember() {
        return member;
    }

    public List<CartItemWithId> getCartItems() {
        return cartItems;
    }
}
