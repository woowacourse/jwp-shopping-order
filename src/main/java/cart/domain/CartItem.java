package cart.domain;

import cart.entity.ProductEntity;
import cart.exception.CartItemException;

import java.util.Objects;

public class CartItem {
    private Long id;
    private int quantity;
    private final ProductEntity productEntity;
    private final Member member;

    public CartItem(Member member, ProductEntity productEntity) {
        this.quantity = 1;
        this.member = member;
        this.productEntity = productEntity;
    }

    public CartItem(Long id, int quantity, ProductEntity productEntity, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.productEntity = productEntity;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public ProductEntity getProduct() {
        return productEntity;
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
}
