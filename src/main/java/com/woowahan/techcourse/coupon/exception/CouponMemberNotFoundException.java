package com.woowahan.techcourse.coupon.exception;

import com.woowahan.techcourse.common.ui.ApiException;

public class CouponMemberNotFoundException extends ApiException {

    public CouponMemberNotFoundException() {
        super("쿠폰을 사용할 수 없는 회원입니다.");
    }
}
