package cart.domain;

import cart.exception.IllegalAccessCartException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private final Member member;
    private final Product product;
    private final Quantity quantity;

    public CartItem(final Long id, final Member member, final Product product, final Quantity quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(final Member member, final Product product) {
        this(null, member, product, new Quantity(Quantity.DEFAULT_VALUE));
    }

    public void validateOwner(final Member otherMember) {
        if (!Objects.equals(this.member.getId(), otherMember.getId())) {
            throw new IllegalAccessCartException(this, otherMember);
        }
    }

    public CartItem changeQuantity(final Quantity quantity) {
        return new CartItem(id, member, product, quantity);
    }

    public Price calculateTotalProductPrice() {
        int result = product.calculateTotalPrice(quantity.getValue());

        return new Price(result);
    }

    public int getQuantityValue() {
        return quantity.getValue();
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

    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id) && Objects.equals(member, cartItem.member) && Objects.equals(product, cartItem.product) && Objects.equals(quantity, cartItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, product, quantity);
    }
}
