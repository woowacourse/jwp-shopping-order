package com.woowahan.techcourse.coupon.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class CalculateActualPriceRequest {

    private final OrderRequest orderRequest;
    private final List<Long> couponIds;

    public CalculateActualPriceRequest(OrderRequest orderRequest, List<Long> couponIds) {
        this.orderRequest = orderRequest;
        this.couponIds = couponIds;
    }

    public OrderRequest getOrderRequest() {
        return orderRequest;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public static class OrderRequest {

        private final BigDecimal originalPrice;

        public OrderRequest(BigDecimal originalPrice) {
            this.originalPrice = originalPrice;
        }

        public BigDecimal getOriginalPrice() {
            return originalPrice;
        }
    }
}
