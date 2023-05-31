package cart.domain;

import java.util.List;

public class Order {

    private final List<CartItem> cartItems;
    private final Money deliveryFee;

    public Order(List<CartItem> cartItems, int deliveryFee) {
        this(cartItems, new Money(deliveryFee));
    }

    public Order(List<CartItem> cartItems, Money deliveryFee) {
        this.cartItems = cartItems;
        this.deliveryFee = deliveryFee;
    }

    public Money calculateTotalPrice() {
        Money totalCartsPrice = cartItems.stream()
                .map(CartItem::calculateCartPrice)
                .reduce(new Money(0), Money::add);
        return totalCartsPrice.add(deliveryFee);
    }
}
