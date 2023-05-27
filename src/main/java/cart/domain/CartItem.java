package cart.domain;

import cart.domain.common.Money;
import cart.exception.InvalidCartItemOwnerException;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private final Member member;
    private final Product product;
    private Integer quantity;

    public CartItem(final Member member, final Product product) {
        this(null, 1, member, product);
    }

    public CartItem(final Long id, final Integer quantity, final Member member, final Product product) {
        this.id = id;
        this.quantity = quantity;
        this.member = member;
        this.product = product;
    }

    public Money calculateTotalPrice() {
        return product.getPrice().times(quantity);
    }

    public void checkOwner(final Member member) {
        if (!this.member.equals(member)) {
            throw new InvalidCartItemOwnerException();
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
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
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }
}
