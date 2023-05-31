package cart.domain;

public class CartItem {

    private final long id;
    private final Member member;
    private final Product product;
    private final Quantity quantity;

    public CartItem(final long id, final Member member, final Product product, final Quantity quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
