package com.woowahan.techcourse.coupon.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문이_잘_생성된다() {
        // given
        OriginalAmount originalAmount = new OriginalAmount(10000);

        // when
        Money result = originalAmount.getOriginalPrice();

        // then
        assertEquals(BigDecimal.valueOf(10000), result.getValue());
    }
}
