package com.woowahan.techcourse.coupon.application.dto;

import java.util.List;

public class CouponExpireRequest {

    private final long memberId;
    private final List<Long> couponIds;

    public CouponExpireRequest(long memberId, List<Long> couponIds) {
        this.memberId = memberId;
        this.couponIds = couponIds;
    }

    public long getMemberId() {
        return memberId;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }
}
