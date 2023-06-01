package com.woowahan.techcourse.order.domain;

import java.math.BigDecimal;

public class OrderResult {

    private final long originalPrice;
    private final BigDecimal actualPrice;
    private final Order order;

    public OrderResult(long originalPrice, BigDecimal actualPrice, Order order) {
        this.originalPrice = originalPrice;
        this.actualPrice = actualPrice;
        this.order = order;
    }

    public OrderResult(long originalPrice, long actualPrice, Order order) {
        this(originalPrice, BigDecimal.valueOf(actualPrice), order);
    }

    public Long getId() {
        return order.getOrderId();
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public Order getOrder() {
        return order;
    }
}
