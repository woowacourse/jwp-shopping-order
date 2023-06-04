package cart.domain.cart;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.value.Quantity;
import cart.exception.cart.IllegalMemberException;

import java.util.Objects;

public class CartItem {
    private final Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Long id, final Quantity quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public CartItem(final Member member, final Product product) {
        this(null, new Quantity(1), product, member);
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this(id, new Quantity(quantity), product, member);
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
        return quantity.getQuantity();
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMemberException();
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = new Quantity(quantity);
    }
}
