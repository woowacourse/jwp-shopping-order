package com.woowahan.techcourse.order.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final Long orderId;
    private final long memberId;
    private final List<OrderItem> orderItems;
    private final List<OrderCoupon> orderCoupons;
    private final BigDecimal originalPrice;
    private final BigDecimal actualPrice;

    public Order(Long orderId, long memberId, List<OrderItem> orderItems, List<OrderCoupon> orderCoupons,
            BigDecimal originalPrice, BigDecimal actualPrice) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.orderCoupons = orderCoupons;
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
    }

    public Order(long memberId,
            List<OrderItem> orderItems,
            List<OrderCoupon> orderCoupons,
            ActualPriceCalculator actualPriceCalculator) {
        orderId = null;
        this.memberId = memberId;
        this.orderItems = orderItems;
        this.orderCoupons = orderCoupons;
        originalPrice = calculateOriginalPrice();
        actualPrice = calculateActualPrice(actualPriceCalculator);
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getMemberId() {
        return memberId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public List<Long> getProductIds() {
        return orderItems.stream()
                .map(OrderItem::getProductId)
                .collect(Collectors.toList());
    }

    public List<Long> getCouponIds() {
        return orderCoupons.stream()
                .map(OrderCoupon::getId)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateOriginalPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::calculateOriginalPrice)
                .mapToObj(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateActualPrice(ActualPriceCalculator actualPriceCalculator) {
        return actualPriceCalculator.calculate(this);
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }
}
