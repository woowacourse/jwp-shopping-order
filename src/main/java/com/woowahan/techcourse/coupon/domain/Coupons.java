package com.woowahan.techcourse.coupon.domain;

import java.util.List;

public class Coupons {

    private final List<Coupon> coupons;

    public Coupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public Money calculateActualPrice(OriginalAmount originalAmount) {
        Money originalPrice = originalAmount.getOriginalPrice();
        Money discountAmount = calculateDiscountAmount(originalAmount);
        return calculateSubtractOrZero(originalPrice, discountAmount);
    }

    private Money calculateSubtractOrZero(Money originalPrice, Money discountAmount) {
        if (discountAmount.isBiggerThan(originalPrice)) {
            return Money.ZERO;
        }
        return originalPrice.subtract(discountAmount);
    }

    private Money calculateDiscountAmount(OriginalAmount originalAmount) {
        return coupons.stream()
                .map(coupon -> coupon.calculateDiscountAmount(originalAmount))
                .reduce(Money.ZERO, Money::add);
    }
}
