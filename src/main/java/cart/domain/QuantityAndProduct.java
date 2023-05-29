package cart.domain;

public class QuantityAndProduct {

    private final int quantity;
    private final Product product;

    public QuantityAndProduct(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }
}
