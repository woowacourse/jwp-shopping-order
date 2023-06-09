package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private final Product product;
    private final Member member;
    private Long id;
    private int quantity;

    public CartItem(final Member member, final Product product) {
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public Long getId() {
        return this.id;
    }

    public Member getMember() {
        return this.member;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void checkOwner(final Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMemberException(this, member);
        }
    }

    public void changeQuantity(final int quantity) {
        this.quantity = quantity;
    }
}
