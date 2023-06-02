package com.woowahan.techcourse.cart.exception;

import com.woowahan.techcourse.cart.domain.CartItem;

public class IllegalMemberException extends CartItemException {

    public IllegalMemberException(CartItem cartItem, long memberId) {
        super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + memberId);
    }
}
