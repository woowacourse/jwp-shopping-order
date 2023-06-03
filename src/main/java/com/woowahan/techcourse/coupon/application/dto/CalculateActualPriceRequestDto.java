package com.woowahan.techcourse.coupon.application.dto;

import java.util.List;

public class CalculateActualPriceRequestDto {

    private final OrderRequest orderRequest;
    private final List<Long> couponIds;

    public CalculateActualPriceRequestDto(OrderRequest orderRequest, List<Long> couponIds) {
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

        private final long originalPrice;

        public OrderRequest(long originalPrice) {
            this.originalPrice = originalPrice;
        }

        public long getOriginalPrice() {
            return originalPrice;
        }
    }
}
