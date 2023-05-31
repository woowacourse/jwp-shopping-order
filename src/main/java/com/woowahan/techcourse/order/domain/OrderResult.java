package com.woowahan.techcourse.order.domain;

public class OrderResult {

    private final long originalPrice;
    private final long actualPrice;
    private final Order order;

    public OrderResult(long originalPrice, long actualPrice, Order order) {
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
        this.order = order;
    }

    public Long getId() {
        return order.getOrderId();
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public long getActualPrice() {
        return actualPrice;
    }

    public Order getOrder() {
        return order;
    }
}
