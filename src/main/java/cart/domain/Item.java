package cart.domain;

public class Item {

    private final Product product;
    private final int quantity;

    public Item(final Product product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
