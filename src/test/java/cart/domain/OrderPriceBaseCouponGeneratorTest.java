package cart.domain;

import cart.domain.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static cart.fixtures.OrderFixtures.ORDER1;
import static cart.fixtures.OrderFixtures.ORDER2;
import static org.assertj.core.api.Assertions.assertThat;

class OrderPriceBaseCouponGeneratorTest {

    private OrderBaseCouponGenerator orderBaseCouponGenerator = new OrderPriceBaseCouponGenerator();

    @Test
    @DisplayName("주문 금액이 50000원 이상이면 보너스 쿠폰이 발급된다.")
    void generateBonusCouponTest_generate() {
        // given
        Order order = ORDER1;

        // when
        Optional<Coupon> coupon = orderBaseCouponGenerator.generate(order);

        // then
        assertThat(coupon).isNotEmpty();
    }

    @Test
    @DisplayName("주문 금액이 50000원 미만이면 보너스 쿠폰이 발급된다.")
    void generateBonusCouponTest_notGenerate() {
        // given
        Order order = ORDER2;

        // when
        Optional<Coupon> coupon = orderBaseCouponGenerator.generate(order);

        // then
        assertThat(coupon).isEmpty();
    }
}