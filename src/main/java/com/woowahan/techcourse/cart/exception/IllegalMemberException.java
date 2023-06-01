package com.woowahan.techcourse.cart.exception;

import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.member.domain.Member;

public class IllegalMemberException extends CartItemException {

    public IllegalMemberException(CartItem cartItem, Member member) {
        super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
    }
}
