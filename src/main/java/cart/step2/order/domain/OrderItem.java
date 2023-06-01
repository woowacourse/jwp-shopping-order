package cart.step2.order.domain;

import cart.domain.Product;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final int quantity;

    private OrderItem(final Long id, final Product product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public static OrderItem createNonePkOrder(final Product product, final int quantity) {
        return new OrderItem(null, product, quantity);
    }

    public static OrderItem of(final Long id, final Product product, final int quantity) {
        return new OrderItem(id, product, quantity);
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
