package cart.domain;

public class CartItem {

    public static final int INITIAL_QUANTITY = 1;
    private final Long id;
    private final Member member;
    private final Product product;
    private final int quantity;

    public CartItem(final Long id, final Member member, final Product product, final int quantity) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public CartItem(final Member member, final Product product) {
        this(null, member, product, INITIAL_QUANTITY);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public CartItem changeQuantity(final int quantity) {
        return new CartItem(id, member, product, quantity);
    }
}
