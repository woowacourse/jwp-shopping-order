package cart.domain;

public class Item {

    private final Long id;
    private final Product product;
    private final int quantity;

    public Item(final Product product, final int quantity) {
        this.id = null;
        this.product = product;
        this.quantity = quantity;
    }

    public Item(final Long id, final Product product, final int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
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
