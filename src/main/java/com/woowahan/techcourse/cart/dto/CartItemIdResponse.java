package com.woowahan.techcourse.cart.dto;

public class CartItemIdResponse {

    private long cartItemId;

    private CartItemIdResponse() {
    }

    public CartItemIdResponse(long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public long getCartItemId() {
        return cartItemId;
    }
}
