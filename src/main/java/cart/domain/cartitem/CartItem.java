package cart.domain.cartitem;

import cart.domain.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;

import java.util.Objects;
import java.util.Optional;

public class CartItem {
    private Long id;
    private Quantity quantity;
    private Product product;
    private Member member;

    private CartItem(){
    }

    private CartItem(final Long id, final Quantity quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public static CartItem create() {
        return new CartItem();
    }

    public static CartItem of(final Member member, final Product product) {
        return new CartItem(null, Quantity.from(1), product, member);
    }

    public static CartItem of(final Long id, final int quantity, final Product product, final Member member) {
        return new CartItem(id, Quantity.from(quantity), product, member);
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = Quantity.from(quantity);
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
}
