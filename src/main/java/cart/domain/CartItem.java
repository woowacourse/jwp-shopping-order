package cart.domain;

public class CartItem {

    private final Long id;
    private final long memberId;
    private final Product product;
    private final Quantity quantity;

    public CartItem(final Long id, final long memberId, final Product product, final Quantity quantity) {
        this.id = id;
        this.memberId = memberId;
        this.product = product;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getMemberId() {
        return memberId;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
