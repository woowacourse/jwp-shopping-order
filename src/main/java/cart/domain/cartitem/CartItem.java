package cart.domain.cartitem;

import static cart.exception.CartItemException.IllegalMember;

import cart.domain.member.Member;
import cart.domain.product.Product;
import java.util.Objects;

public class CartItem {

    private Long id;
    private Quantity quantity;
    private Member member;
    private Product product;

    private CartItem() {
    }

    public CartItem(Member member, Product product) {
        this(null, 1, member, product);
    }

    public CartItem(Long id, int quantity, Member member, Product product) {
        this.id = id;
        this.quantity = new Quantity(quantity);
        this.member = member;
        this.product = product;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity.getValue();
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMember();
        }
    }

    public CartItem changeQuantity(int quantity) {
        return new CartItem(id, quantity, member, product);
    }
}
