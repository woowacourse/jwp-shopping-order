package com.woowahan.techcourse.cart.domain;

import com.woowahan.techcourse.cart.exception.IllegalMemberException;
import com.woowahan.techcourse.member.domain.Member;
import com.woowahan.techcourse.product.domain.Product;
import java.util.Objects;

public class CartItem {

    private static final int DEFAULT_QUANTITY = 1;

    private final Long id;
    private final Product product;
    private final long memberId;
    private int quantity;

    public CartItem(Long id, int quantity, Product product, long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.memberId = memberId;
    }

    public CartItem(Product product, long memberId) {
        this(null, DEFAULT_QUANTITY, product, memberId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
        if (!Objects.equals(memberId, member.getId())) {
            throw new IllegalMemberException(this, member);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
