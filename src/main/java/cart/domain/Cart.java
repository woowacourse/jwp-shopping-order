package cart.domain;

import cart.domain.Member.Member;
import cart.exception.NotFoundException;

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

    public void checkHavingAll(List<Long> searchCartIds) {
        if (cartItems.size() != searchCartIds.size()) {
            throw new NotFoundException.CartItem(searchCartIds);
        }
    }
}
