package com.woowahan.techcourse.cart.domain;

import com.woowahan.techcourse.cart.exception.IllegalMemberException;
import com.woowahan.techcourse.member.domain.Member;
import com.woowahan.techcourse.product.domain.Product;
import java.util.Objects;

public class CartItem {

    private static final int DEFAULT_QUANTITY = 1;
    private final Product product;
    private final Member member;
    private final Long id;
    private int quantity;

    public CartItem(Long id, int quantity, Product product, Member member) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.member = member;
    }

    public CartItem(Member member, Product product) {
        this(null, DEFAULT_QUANTITY, product, member);
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

    public Long getProductId() {
        return product.getId();
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
