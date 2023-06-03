package cart.domain;

import cart.domain.Member.Member;

import java.util.List;
import java.util.stream.Collectors;

public class Cart {
    private final List<CartItem> cartItems;

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void checkOwner(Member member) {
        cartItems.forEach(it -> it.checkOwner(member));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public List<Long> getCartIds() {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toUnmodifiableList());
    }
}
