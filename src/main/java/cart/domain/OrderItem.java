package cart.domain;

public class OrderItem {

    private final Long id;
    private final Product product;
    private final Quantity quantity;

    public OrderItem(final Long id, final Product product, final Quantity quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItem(final Product product, final Quantity quantity) {
        this(null, product, quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
