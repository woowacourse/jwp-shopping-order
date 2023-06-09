package cart.domain;

import cart.exception.CartItemException;
import cart.exception.ErrorMessage;
import java.util.Objects;

public class CartItem {
    private final Product product;
    private final Member member;
    private final Long id;
    private final Quantity quantity;

    public CartItem(Member member, Product product) {
        this(null, 1, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = new Quantity(quantity);
        this.product = product;
        this.member = member;
    }

    public void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new CartItemException(ErrorMessage.INVALID_CART_ITEM_OWNER);
        }
    }

    public boolean isSameOwner(Member member) {
        return this.member.equals(member);
    }

    public CartItem changeQuantity(int quantity) {
        return new CartItem(id, quantity, product, member);
    }

    public int calculatePrice() {
        return product.getPrice() * quantity.getValue();
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
