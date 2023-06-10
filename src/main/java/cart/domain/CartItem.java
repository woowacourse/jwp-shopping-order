package cart.domain;

import cart.exception.CartItemException;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(final int quantity, final Product product, final Member member) {
        this(null, quantity, product, member);
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public CartItem addQuantity(final int quantity) {
        return new CartItem(id, this.quantity + quantity, product, member);
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
