package com.woowahan.techcourse.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 총_금액_계산을_정상적으로_수행한다() {
        Order given = OrderFixture.firstOrder;

        assertThat(given.calculateOriginalPrice().longValue()).isEqualTo(1000L);
    }

    @Test
    void 총_금액_계산을_정상적으로_수행한다2() {
        Order given = OrderFixture.secondOrder;

        assertThat(given.calculateOriginalPrice().longValue()).isEqualTo(4000L);
    }
}
