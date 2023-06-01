package cart.domain;

import cart.domain.value.Quantity;
import cart.exception.CartItemException;
import java.util.Objects;

public class CartItem {
    private final Long id;
    private Quantity quantity;
    private final Member member;
    private final Product product;

    public CartItem(final Member member, final Product product) {
        this.id = null;
        this.quantity = new Quantity(1);
        this.member = member;
        this.product = product;
    }

    public CartItem(final Long id, final int quantity, final Member member, final Product product) {
        this.id = id;
        this.quantity = new Quantity(quantity);
        this.member = member;
        this.product = product;
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
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = new Quantity(quantity);
    }
}
