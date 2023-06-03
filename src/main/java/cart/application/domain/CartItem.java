package cart.application.domain;

import cart.application.exception.IllegalMemberException;

import java.util.Objects;

public class CartItem {

    private final Long id;
    private final long quantity;
    private final Product product;
    private final Member member;

    public CartItem(Long id, long quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public void validateIsOwnedBy(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new IllegalMemberException();
        }
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

    public long getQuantity() {
        return quantity;
    }
}
