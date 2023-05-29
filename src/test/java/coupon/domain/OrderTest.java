package coupon.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문이_잘_생성된다() {
        // given
        Order order = new Order(10000);

        // when
        Money result = order.getOriginalPrice();

        // then
        assertEquals(10000, result.getValue());
    }
}
