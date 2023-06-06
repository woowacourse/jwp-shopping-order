package com.woowahan.techcourse.cart.exception;

import com.woowahan.techcourse.common.ui.ApiException;

public class CartItemNotFoundException extends ApiException {

    public CartItemNotFoundException() {
        super("해당 상품이 장바구니에 존재하지 않습니다.");
    }
}
