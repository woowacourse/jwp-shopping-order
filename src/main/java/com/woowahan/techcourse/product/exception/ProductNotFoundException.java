package com.woowahan.techcourse.product.exception;

import com.woowahan.techcourse.common.ui.ApiException;

public class ProductNotFoundException extends ApiException {

    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }
}
