package cart.domain;

import cart.exception.UnauthorizedAccessException;
import java.util.Objects;

public class CartItem {
    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(final int quantity, final Member member, final Product product) {
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
            throw new UnauthorizedAccessException(member.getEmail(), this.id);
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public int calculateTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CartItem cartItem = (CartItem) o;
        return getQuantity() == cartItem.getQuantity() && Objects.equals(getId(), cartItem.getId())
                && Objects.equals(getProduct(), cartItem.getProduct()) && Objects.equals(getMember(),
                cartItem.getMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuantity(), getProduct(), getMember());
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
