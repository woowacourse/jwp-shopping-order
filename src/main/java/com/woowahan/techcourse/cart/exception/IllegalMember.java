package com.woowahan.techcourse.cart.exception;

import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.member.domain.Member;

public class IllegalMember extends CartItemException {

    public IllegalMember(CartItem cartItem, Member member) {
        super("Illegal member attempts to cart; cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
    }
}
