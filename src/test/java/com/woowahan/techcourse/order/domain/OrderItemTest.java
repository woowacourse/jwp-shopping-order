package com.woowahan.techcourse.order.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderItemTest {

    @ParameterizedTest
    @CsvSource(value = {"1, 1000, 1000", "2, 1000, 2000", "3, 1000, 3000"})
    void 주문_상품_총액_계산_테스트(int quantity, int price, long expected) {
        // given
        OrderItem orderItem = new OrderItem(quantity, 1L, price, "name", "imageUrl");

        // when
        long actual = orderItem.calculateOriginalPrice();

        // then
        assertEquals(expected, actual);
    }
}
