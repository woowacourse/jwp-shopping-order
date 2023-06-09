package cart.domain.order;

import cart.domain.cart.Quantity;
import cart.domain.product.Price;
import cart.domain.product.Product;

public class OrderItem {

    private final Long id;
    private final Quantity quantity;
    private final Product product;

    public OrderItem(final Quantity quantity, final Product product) {
        this(null, quantity, product);
    }

    public OrderItem(
            final Long id,
            final Quantity quantity,
            final Product product
    ) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Price getTotalPrice() {
        return product.getPrice().multiply(quantity.quantity());
    }

    public Long getId() {
        return id;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
