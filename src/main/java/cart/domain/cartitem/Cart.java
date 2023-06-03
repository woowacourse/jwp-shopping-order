package cart.domain.cartitem;

import cart.domain.cartitem.dto.CartItemWithId;
import cart.domain.member.Member;
import cart.exception.ErrorCode;
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

    public void checkOwner(final String memberName) {
        if (!Objects.equals(this.member.name(), memberName)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
    }

    public Member getMember() {
        return member;
    }

    public List<CartItemWithId> getCartItems() {
        return cartItems;
    }
}
