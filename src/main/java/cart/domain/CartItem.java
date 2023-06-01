package cart.domain;

import cart.exception.CartItemException;
import cart.exception.InvalidOrderCheckedException;
import cart.exception.InvalidOrderProductException;
import cart.exception.InvalidOrderQuantityException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;
    private boolean checked;

    public CartItem(Member member, Product product) {
        this(null, 1, product, member, true);
    }

    public CartItem(Long id, int quantity, Product product, Member member, final boolean checked) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isSameId(Long otherId) {
        return id.equals(otherId);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void changeChecked(final boolean checked) {
        this.checked = checked;
    }

    public void validateSameItems(final CartItem other) {
        validateSameProductValues(other);
        validateSameQuantity(other);
        validateSameChecked(other);
    }


    private void validateSameProductValues(final CartItem other) {
        if (!product.equals(other.product) || !product.hasSameValues(other.product)) {
            throw new InvalidOrderProductException();
        }
    }

    private void validateSameQuantity(final CartItem other) {
        if (quantity != other.quantity) {
            throw new InvalidOrderQuantityException();
        }
    }

    private void validateSameChecked(final CartItem other) {
        if (checked != other.checked) {
            throw new InvalidOrderCheckedException();
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem other = (CartItem) o;
        if (id == null || other.id == null) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
