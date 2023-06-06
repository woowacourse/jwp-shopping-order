package com.woowahan.techcourse.order.exception;

import com.woowahan.techcourse.common.ui.ApiException;

public class OrderNotFoundException extends ApiException {

    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }
}
