package cart.domain.order;

import cart.domain.cart.Quantity;
import cart.domain.product.Price;
import cart.domain.product.Product;

public class OrderItem {

    private final Long id;
    private final Long orderId;
    private final Quantity quantity;
    private final Product product;

    public OrderItem(final Quantity quantity, final Product product) {
        this(null, null, quantity, product);
    }

    public OrderItem(
            final Long id,
            final Long orderId,
            final Quantity quantity,
            final Product product
    ) {
        this.id = id;
        this.orderId = orderId;
        this.quantity = quantity;
        this.product = product;
    }

    public Price getTotalPrice() {
        return product.getPrice().multiply(quantity.quantity());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
