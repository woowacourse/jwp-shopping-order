package cart.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cart.exception.NotContainedItemException;

public class Cart {

    private final Member owner;
    private final List<CartItem> cartItems;

    public Cart(Member owner, List<CartItem> cartItems) {
        this.owner = owner;
        this.cartItems = new ArrayList<>(cartItems);
    }

    public List<OrderItem> order(List<CartItem> itemsToOrder) {
        validateContainsAllOf(itemsToOrder);
        cartItems.removeAll(itemsToOrder);
        return itemsToOrder.stream()
                .map(OrderItem::new)
                .collect(Collectors.toList());
    }

    private void validateContainsAllOf(List<CartItem> cartItems) {
        Set<CartItem> reference = new HashSet<>(this.cartItems);
        if (reference.containsAll(cartItems)) {
            return;
        }
        throw new NotContainedItemException();
    }

    public Member getOwner() {
        return owner;
    }

    public List<CartItem> getItems() {
        return cartItems;
    }
}
