package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private final Long id;
    private final int quantity;
    private final Product product;
    private final Member member;

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public CartItem(Member member, Product product, int quantity) {
        this(null, quantity, product, member);
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

    public CartItem changeQuantity(int quantity) {
        return new CartItem(id, quantity, product, member);
    }
}
