package cart.domain.product;

public abstract class Item {
    protected Long id;
    protected int quantity;
    protected final Product product;

    public Item(Product product) {
        this.quantity = 1;
        this.product = product;
    }

    public Item(Long id, int quantity, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public final Long getId() {
        return id;
    }

    public final Product getProduct() {
        return product;
    }

    public final int getQuantity() {
        return quantity;
    }
}
