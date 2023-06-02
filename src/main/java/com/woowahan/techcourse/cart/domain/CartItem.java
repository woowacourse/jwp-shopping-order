package com.woowahan.techcourse.cart.domain;

import com.woowahan.techcourse.cart.exception.IllegalMemberException;
import java.util.Objects;

public class CartItem {

    private static final int DEFAULT_QUANTITY = 1;

    private final Long id;
    private final long productId;
    private final long memberId;
    private int quantity;

    public CartItem(Long id, int quantity, long productId, long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.memberId = memberId;
    }

    public CartItem(long productId, long memberId) {
        this(null, DEFAULT_QUANTITY, productId, memberId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void checkOwner(long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new IllegalMemberException(this, memberId);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
