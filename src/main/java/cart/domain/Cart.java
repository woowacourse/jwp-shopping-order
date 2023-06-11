package cart.domain;

import java.util.ArrayList;
import java.util.List;

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

    public Order order(CartItem itemToOrder) {
        validateContains(itemToOrder);
        cartItems.remove(itemToOrder);
        return Order.of(owner, List.of(itemToOrder));
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
