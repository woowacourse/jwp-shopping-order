package cart.domain.order;

import cart.domain.Product;
import cart.domain.cart.CartItem;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final int quantity;

    public OrderItem(final Product product, final int quantity) {
        this(null, product, quantity);
    }

    public OrderItem(final Long id, final Product product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderItem of(final CartItem cartItem) {
        return new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
    }
    
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
