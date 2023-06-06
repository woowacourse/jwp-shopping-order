package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final long quantity;

    public OrderItem(final Long id, final Product product, final long quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderItem of(final Product product, final long quantity) {
        return new OrderItem(null, product, quantity);
    }

    public static OrderItem of(final CartItem cartItem) {
        return new OrderItem(null, cartItem.getProduct(), cartItem.getQuantity());
    }

    public static List<OrderItem> of(final List<CartItem> cartItems) {
        return cartItems.stream()
                .map(OrderItem::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
