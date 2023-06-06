package com.woowahan.techcourse.coupon.ui.response;

import com.woowahan.techcourse.coupon.domain.Coupon;

public class CouponResponse {

    private long id;
    private String type;
    private int amount;
    private String name;

    private CouponResponse() {
    }

    public CouponResponse(long id, String type, int amount, String name) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.name = name;
    }

    public CouponResponse(Coupon coupon) {
        this(coupon.getCouponId(), coupon.getDiscountPolicy().getName(), coupon.getAmount(),
                coupon.getName());
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
