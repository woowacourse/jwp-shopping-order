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

    public void applyCouponsOn(CartItem item, List<MemberCoupon> coupons) {
        validateContains(item);
        int index = cartItems.indexOf(item);
        cartItems.get(index).apply(coupons);
    }

    public Order order(List<CartItem> itemsToOrder) {
        validateContains(itemsToOrder);
        cartItems.removeAll(itemsToOrder);
        List<OrderItem> orderItems = itemsToOrder.stream()
                .map(OrderItem::new)
                .collect(Collectors.toList());
        return new Order(owner, orderItems);
    }

    private void validateContains(List<CartItem> items) {
        for (CartItem item : items) {
            validateContains(item);
        }
    }

    private void validateContains(CartItem item) {
        if (!cartItems.contains(item)) {
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
