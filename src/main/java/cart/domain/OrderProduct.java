package cart.domain;

public class OrderProduct {

    private final Product product;
    private final Quantity quantity;

    public OrderProduct(final Product product, final Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}