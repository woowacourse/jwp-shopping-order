package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private final Long id;
    private final Product product;
    private final Member member;

    private Integer quantity;

    public CartItem(Member member, Product product) {
        this.id = null;
        this.quantity = 1;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, Integer quantity, Product product, Member member) {
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
        return quantity * product.getPrice().getValue();
    }

    public void changeQuantity(Integer quantity) {
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

    public Integer getQuantity() {
        return quantity;
    }
}
