package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private final Long id;
    private final int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.id = null;
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

    public OrderItem toOrderItem() {
        return new OrderItem(null, product, quantity);
    }

    public Integer calculatePrice() {
        return product.getPrice() * quantity;
    }

    public void checkValue(final CartItem other) {
        if (!Objects.equals(this.quantity, other.quantity)) {
            throw new CartItemException.QuantityIncorrect("카트 수량이 변경되었습니다.");
        }
        this.product.checkValue(other.product);
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember("접근 권한 없습니다");
        }
    }

    public CartItem changeQuantity(int changedQuantity) {
        return new CartItem(
                id,
                changedQuantity,
                product,
                member
        );
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
