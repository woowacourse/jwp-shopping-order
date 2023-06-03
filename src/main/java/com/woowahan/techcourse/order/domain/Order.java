package com.woowahan.techcourse.order.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final Long orderId;
    private final long memberId;
    private final List<OrderItem> orderItems;
    private final List<OrderCoupon> orderCoupons;


    public Order(Long orderId, long memberId, List<OrderItem> orderItems, List<OrderCoupon> orderCoupons) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.orderCoupons = orderCoupons;
    }

    public Order(long memberId, List<OrderItem> orderItems, List<OrderCoupon> orderCoupons) {
        this(null, memberId, orderItems, orderCoupons);
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

    public List<Long> getCouponIds() {
        return orderCoupons.stream()
                .map(OrderCoupon::getId)
                .collect(Collectors.toList());
    }

    public long calculateOriginalPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::calculateOriginalPrice)
                .sum();
    }
}
