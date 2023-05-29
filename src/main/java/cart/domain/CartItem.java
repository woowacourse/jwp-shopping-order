package cart.domain;

import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {

    private Long id;
    private Long quantity;
    private final Member member;
    private final Product product;

    public CartItem(Member member, Product product) {
        this.quantity = 1L;
        this.member = member;
        this.product = product;
    }

    public CartItem(Long id, Long quantity, Member member, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.member = member;
        this.product = product;
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

    public void checkOwner(Member member) {
        if (!Objects.equals(this.member.getId(), member.getId())) {
            throw new CartItemException.IllegalMember(this, member);
        }
    }

    public void changeQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
