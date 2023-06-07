package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private static final int INITIAL_QUANTITY = 1;

    private final Long id;
    private final Quantity quantity;
    private final Product product;
    private final Member member;

    public CartItem(Member member, Product product) {
        this.id = null;
        this.quantity = new Quantity(INITIAL_QUANTITY);
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, Quantity quantity, Product product, Member member) {
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

    public long calculateTotalPrice() {
        return quantity.getValue() * product.getPrice().getValue();
    }

    public CartItem changeQuantity(Quantity quantity) {
        return new CartItem(id, quantity, product, member);
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

    public Integer getQuantity() {
        return quantity.getValue();
    }
}
