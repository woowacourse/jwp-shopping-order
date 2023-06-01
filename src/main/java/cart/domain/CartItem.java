package cart.domain;

import cart.exception.CartItemException;
import cart.exception.InvalidOrderCheckedException;
import cart.exception.InvalidOrderProductException;
import cart.exception.InvalidOrderQuantityException;

import java.util.Objects;

public class CartItem {
    private Long id;
    private int quantity;
    private final Product product;
    private final Member member;
    private boolean checked;

    public CartItem(Member member, Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
        this.checked = true;
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
        validateSameProduct(other);
        validateSameQuantity(other);
        validateSameChecked(other);
    }


    private void validateSameProduct(final CartItem other) {
        if (!product.equals(other.product)) {
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity
                && checked == cartItem.checked
                && Objects.equals(id, cartItem.id)
                && Objects.equals(product, cartItem.product)
                && Objects.equals(member, cartItem.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product, member, checked);
    }
}
