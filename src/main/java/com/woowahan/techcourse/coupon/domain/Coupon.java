package com.woowahan.techcourse.coupon.domain;

import com.woowahan.techcourse.coupon.domain.discountcondition.DiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountpolicy.DiscountPolicy;
import java.util.Objects;

public class Coupon {

    private final Long couponId;
    private final CouponName name;
    private final DiscountCondition discountCondition;
    private final DiscountPolicy discountPolicy;

    public Coupon(Long couponId, String name, DiscountCondition discountCondition, DiscountPolicy discountPolicy) {
        this.couponId = couponId;
        this.name = new CouponName(name);
        this.discountCondition = discountCondition;
        this.discountPolicy = discountPolicy;
    }

    public Coupon(String name, DiscountCondition discountCondition, DiscountPolicy discountPolicy) {
        this(null, name, discountCondition, discountPolicy);
    }

    public Money calculateDiscountAmount(Order order) {
        if (discountCondition.isSatisfiedBy(order)) {
            return discountPolicy.calculateDiscountAmount(order);
        }
        return Money.ZERO;
    }

    public CouponName getName() {
        return name;
    }

    public Long getCouponId() {
        return couponId;
    }

    public DiscountCondition getDiscountCondition() {
        return discountCondition;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public int getAmount() {
        return discountPolicy.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coupon coupon = (Coupon) o;
        return Objects.equals(couponId, coupon.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponId);
    }
}
