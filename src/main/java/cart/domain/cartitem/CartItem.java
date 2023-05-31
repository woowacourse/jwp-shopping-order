package cart.domain.cartitem;

import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final Product product;
    private final Member member;

    public CartItem(final Product product, final Member member) {
        this(null, 1, product, member);
    }

    public CartItem(final int quantity, final Product product, final Member member) {
        this(null, quantity, product, member);
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getProductId() {
        return product.getId();
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

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int calculatePriceOfQuantity() {
        return product.calculatePriceBy(quantity);
    }

}
