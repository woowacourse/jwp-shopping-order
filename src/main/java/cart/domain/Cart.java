package cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cart.exception.NotContainedItemException;

public class Cart {

    private final Member owner;
    private final List<CartItem> cartItems;

    public Cart(Member owner, List<CartItem> cartItems) {
        this.owner = owner;
        this.cartItems = new ArrayList<>(cartItems);
    }

    public Order order(List<CartItem> itemsToOrder) {
        validateContainsAllOf(itemsToOrder);
        cartItems.removeAll(itemsToOrder);
        List<OrderItem> orderItems = itemsToOrder.stream()
                .map(OrderItem::new)
                .collect(Collectors.toList());
        return new Order(owner, orderItems);
    }

    private void validateContainsAllOf(List<CartItem> items) {
        boolean anyAbsence = items.stream()
                .anyMatch(that -> !cartItems.contains(that));
        if (anyAbsence) {
            throw new NotContainedItemException();
        }
    }

    public Member getOwner() {
        return owner;
    }

    public List<CartItem> getItems() {
        return cartItems;
    }
}
