package cart.domain;

public class OrderProduct {
    private final Long id;
    private final Order order;
    private final Product product;
    private final int quantity;

    public OrderProduct(final Order order, final Product product, final int quantity) {
        this(null, order, product, quantity);
    }

    public OrderProduct(final Long id, final Order order, final Product product, final int quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
