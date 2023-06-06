package com.woowahan.techcourse.order.domain;

import java.math.BigDecimal;
import java.util.List;

public class OrderFixture {

    public static final Order firstOrder = new Order(1L,
            1L,
            List.of(OrderItemFixture.firstOrderItem),
            List.of(OrderCouponFixture.firstOrderCoupon),
            BigDecimal.valueOf(1000),
            BigDecimal.valueOf(1000));

    public static final Order secondOrder = new Order(2L,
            1L,
            List.of(OrderItemFixture.secondOrderItem),
            List.of(OrderCouponFixture.secondOrderCoupon),
            BigDecimal.valueOf(4000),
            BigDecimal.valueOf(4000));
}
