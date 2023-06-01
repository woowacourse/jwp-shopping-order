package com.woowahan.techcourse.cart.domain;

import com.woowahan.techcourse.cart.exception.IllegalMemberException;
import com.woowahan.techcourse.member.domain.Member;
import com.woowahan.techcourse.product.domain.Product;
import java.util.Objects;

public class CartItem {

    private final Product product;
    private final Member member;
    private Long id;
    private int quantity;

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public CartItem(Member member, Product product) {
        this(null, 1, product, member);
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
            throw new IllegalMemberException(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
