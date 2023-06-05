package cart.domain.cart;

import cart.domain.Member;
import java.util.List;

public class CartItems {

    private final List<CartItem> cartItems;
    private final Member member;

    public CartItems(final List<CartItem> cartItems, final Member member) {
        this.member = member;
        validateItemsLength(cartItems);
        validateMember(cartItems);
        this.cartItems = cartItems;
    }

    private void validateItemsLength(final List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니 상품이 존재하지 않습니다");
        }
    }

    private void validateMember(final List<CartItem> cartItems) {
        if (containsOtherMemberCartItem(cartItems)) {
            throw new IllegalArgumentException("다른 Member의 장바구니 상품이 포함되어 있습니다");
        }
    }

    private boolean containsOtherMemberCartItem(final List<CartItem> cartItems) {
        return cartItems.stream()
                .anyMatch(cartItem -> !cartItem.getMember().equals(member));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cartItems=" + cartItems +
                ", member=" + member +
                '}';
    }

}
