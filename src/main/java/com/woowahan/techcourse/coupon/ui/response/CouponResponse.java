package com.woowahan.techcourse.coupon.ui.response;

import com.woowahan.techcourse.coupon.domain.Coupon;

public class CouponResponse {

    private Long id;
    private String type;
    private Integer amount;
    private String name;

    private CouponResponse() {
    }

    public CouponResponse(Long id, String type, Integer amount, String name) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.name = name;
    }

    public CouponResponse(Coupon coupon) {
        this(coupon.getCouponId(), coupon.getDiscountPolicy().getName(), coupon.getAmount(),
                coupon.getName().getValue());
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
