package cart.domain;

import java.util.Objects;

import cart.exception.CartItemException;

public class CartItem {

    private final Long id;
    private final Member member;
    private final Product product;
    private final int quantity;

    public CartItem(final Long id, final CartItem other) {
        this(id, other.member, other.product, other.quantity);
    }

    public CartItem(final Member member, final Product product) {
        this(null, member, product, 1);
    }

    public CartItem(final Product product, final int quantity) {
        this(null, null, product, quantity);
    }

    public CartItem(final Long id, final Member member, final Product product, final int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public CartItem changeQuantity(final int quantity) {
        return new CartItem(id, member, product, quantity);
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
}
