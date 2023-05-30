package cart.domain;

import cart.domain.member.MemberValidator;
import cart.exception.InvalidCartItemOwnerException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private final Long memberId;
    private final Product product;
    private Integer quantity;

    public CartItem(final Long member, final Product product) {
        this(null, 1, member, product);
    }

    public CartItem(final Long id, final Integer quantity, final Long member, final Product product) {
        this.id = id;
        this.quantity = quantity;
        this.memberId = member;
        this.product = product;
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public void validateOwner(final MemberValidator memberValidator) {
        if (!memberValidator.isOwner(memberId)) {
            throw new InvalidCartItemOwnerException();
        };
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Product getProduct() {
        return product;
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
}
