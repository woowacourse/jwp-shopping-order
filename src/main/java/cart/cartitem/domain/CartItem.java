package cart.cartitem.domain;

import cart.member.domain.Member;
import cart.product.domain.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private Long id;
    private Long quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.quantity = 1L;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, Long quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }
    
    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }
    
    public void changeQuantity(Long quantity) {
        this.quantity = quantity;
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

    public Long getQuantity() {
        return quantity;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id) && Objects.equals(quantity, cartItem.quantity) && Objects.equals(product, cartItem.product) && Objects.equals(member, cartItem.member);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, product, member);
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
