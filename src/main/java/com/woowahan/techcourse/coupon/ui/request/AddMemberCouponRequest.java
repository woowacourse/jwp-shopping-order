package com.woowahan.techcourse.coupon.ui.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AddMemberCouponRequest {

    @NotNull
    @Positive
    private Long couponId;

    private AddMemberCouponRequest() {
    }

    public AddMemberCouponRequest(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
