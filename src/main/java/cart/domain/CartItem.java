package cart.domain;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private final Product product;
    private final Member member;
    private Long id;
    private int quantity;

    public CartItem(Member member, Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public boolean hasSameProduct(final Long productId) {
        return product.isSameId(productId);
    }

    public void checkProductDuplication(final Long productId) {
        if (Objects.equals(this.product.getId(), productId)) {
            throw new CartItemException.DuplicateProduct(productId);
        }
    }

    public void changeQuantity(int quantity) {
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
        return quantity == cartItem.quantity && Objects.equals(product, cartItem.product) && Objects.equals(member, cartItem.member) && Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, member, id, quantity);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", member=" + member +
                ", id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
