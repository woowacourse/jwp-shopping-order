package com.woowahan.techcourse.order.domain;

public interface ActualPriceCalculator {

    long calculate(Order order);
}
