package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

public class CartItem {

    private static final int DEFAULT_QUANTITY = 1;

    private final Long id;
    private final Member member;
    private final Product product;
    private final Quantity quantity;

    public CartItem(final Long id, final Member member, final Product product, final int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public CartItem(final Member member, final Product product) {
        this(null, member, product, DEFAULT_QUANTITY);
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
        return quantity.getValue();
    }

    public void checkOwner(final Member member) {
        if (!this.member.equals(member)) {
            throw new CartItemException.IllegalMember();
        }
    }

    public void updateQuantity(final int quantity) {
        this.quantity.updateQuantity(quantity);
    }
}
