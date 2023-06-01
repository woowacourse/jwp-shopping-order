package com.woowahan.techcourse.coupon.application.dto;

import java.math.BigDecimal;

public class CalculateActualPriceResponseDto {

    private final BigDecimal actualPrice;

    public CalculateActualPriceResponseDto(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }
}
