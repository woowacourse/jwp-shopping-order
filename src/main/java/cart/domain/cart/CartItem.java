package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

public class CartItem {

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

    public CartItem(final Member member, final Product product, final Integer quantity) {
        this(null, member, product, quantity);
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
