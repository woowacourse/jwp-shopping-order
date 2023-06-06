package com.woowahan.techcourse.coupon.application.dto;

import java.math.BigDecimal;

public class CalculateActualPriceResponse {

    private final BigDecimal actualPrice;

    public CalculateActualPriceResponse(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }
}
