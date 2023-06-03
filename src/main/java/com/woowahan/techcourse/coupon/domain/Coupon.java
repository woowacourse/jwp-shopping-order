package com.woowahan.techcourse.coupon.domain;

import com.woowahan.techcourse.coupon.domain.discountcondition.DiscountCondition;
import com.woowahan.techcourse.coupon.domain.discountpolicy.DiscountPolicy;
import com.woowahan.techcourse.coupon.exception.CouponException;
import java.util.Objects;

public class Coupon {

    private static final int MAX_NAME_LENGTH = 255;

    private final Long couponId;
    private final String name;
    private final DiscountCondition discountCondition;
    private final DiscountPolicy discountPolicy;

    public Coupon(Long couponId, String name, DiscountCondition discountCondition, DiscountPolicy discountPolicy) {
        validate(name);
        this.couponId = couponId;
        this.name = name;
        this.discountCondition = discountCondition;
        this.discountPolicy = discountPolicy;
    }

    public Coupon(String name, DiscountCondition discountCondition, DiscountPolicy discountPolicy) {
        this(null, name, discountCondition, discountPolicy);
    }

    private void validate(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new CouponException("쿠폰 이름은 " + MAX_NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public Money calculateDiscountAmount(OriginalAmount originalAmount) {
        if (discountCondition.isSatisfiedBy(originalAmount)) {
            return discountPolicy.calculateDiscountAmount(originalAmount);
        }
        return Money.ZERO;
    }

    public String getName() {
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
