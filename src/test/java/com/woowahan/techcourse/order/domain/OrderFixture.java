package com.woowahan.techcourse.order.domain;

import java.util.List;

public class OrderFixture {

    public static final Order firstOrder = new Order(1L,
            1L,
            List.of(OrderItemFixture.firstOrderItem),
            List.of(OrderCouponFixture.firstOrderCoupon));

    public static final Order secondOrder = new Order(2L,
            1L,
            List.of(OrderItemFixture.secondOrderItem),
            List.of(OrderCouponFixture.secondOrderCoupon));
}
