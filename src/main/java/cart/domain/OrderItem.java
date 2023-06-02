package cart.domain;

public class OrderItem {

    private final Long id;
    private final int quantity;
    private final Product product;

    public OrderItem(final Long id, final int quantity, final Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
