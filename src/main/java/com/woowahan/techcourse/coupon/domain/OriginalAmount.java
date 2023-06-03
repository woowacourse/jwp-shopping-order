package com.woowahan.techcourse.coupon.domain;

import java.math.BigDecimal;

public class OriginalAmount {

    private final Money originalPrice;

    public OriginalAmount(BigDecimal originalPrice) {
        this.originalPrice = new Money(originalPrice);
    }

    public OriginalAmount(long originalPrice) {
        this.originalPrice = new Money(originalPrice);
    }

    public Money getOriginalPrice() {
        return originalPrice;
    }
}
