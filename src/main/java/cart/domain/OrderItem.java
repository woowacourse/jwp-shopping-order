package cart.domain;

public class OrderItem {
    private final Long id;
    private final Product product;
    private final int quantity;

    public OrderItem(final Long id, final Product product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItem(final Product product, final int quantity) {
        this(null, product, quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }
}
