package com.woowahan.techcourse.order.domain;

public class OrderResultFixture {

    public static final OrderResult firstOrderResult = new OrderResult(1000,
            1000,
            OrderFixture.firstOrder);

    public static final OrderResult secondOrderResult = new OrderResult(4000,
            4000,
            OrderFixture.secondOrder);
}
