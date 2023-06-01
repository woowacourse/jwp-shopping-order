package com.woowahan.techcourse.order.domain;

import java.math.BigDecimal;

public interface ActualPriceCalculator {

    BigDecimal calculate(Order order);
}
