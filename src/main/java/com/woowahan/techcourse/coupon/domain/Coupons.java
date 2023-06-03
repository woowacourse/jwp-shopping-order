package com.woowahan.techcourse.coupon.domain;

import java.util.List;

public class Coupons {

    private final List<Coupon> coupons;

    public Coupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public Money calculateActualPrice(Order order) {
        Money originalPrice = order.getOriginalPrice();
        Money discountAmount = calculateDiscountAmount(order);
        return calculateSubtractOrZero(originalPrice, discountAmount);
    }

    private Money calculateSubtractOrZero(Money originalPrice, Money discountAmount) {
        if (discountAmount.isBiggerThan(originalPrice)) {
            return Money.ZERO;
        }
        return originalPrice.subtract(discountAmount);
    }

    private Money calculateDiscountAmount(Order order) {
        return coupons.stream()
                .map(coupon -> coupon.calculateDiscountAmount(order))
                .reduce(Money.ZERO, Money::add);
    }
}
