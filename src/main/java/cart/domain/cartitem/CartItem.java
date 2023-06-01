package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.authorization.CartItemException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private final Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Member member, final Product product) {
        this(null, member, product, 1);
    }

    public CartItem(final Member member, final Product product, final int quantity) {
        this(null, member, product, quantity);
    }

    public CartItem(final Long id, final Member member, final Product product, final int quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public CartItem(final Long id, final Member member, final Product product, final Quantity quantity) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException(member.getEmail());
        }
    }

    public CartItem updateQuantity(int quantity) {
        return new CartItem(id, member, product, quantity);
    }

    public CartItem addQuantity() {
        return new CartItem(id, member, product, quantity.add());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public int getProductPrice() {
        return product.getPriceValue();
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public int getQuantityValue() {
        return quantity.getQuantity();
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

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                ", member=" + member +
                '}';
    }
}
