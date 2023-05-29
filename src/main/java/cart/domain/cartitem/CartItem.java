package cart.domain.cartitem;

import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private Long id;
    private Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(int quantity, Member member, Product product) {
        this.quantity = new Quantity(quantity);
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = new Quantity(quantity);
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
        return quantity.getQuantity();
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public boolean isSameProduct(Product product) {
        return this.product.equals(product);
    }

    public void changeQuantity(int quantityToChange) {
        this.quantity = quantity.changeQuantity(quantityToChange);
    }

    public void addQuantity(int quantityToAdd) {
        this.quantity = quantity.addQuantity(quantityToAdd);
    }
}
