package cart.domain;

import org.junit.jupiter.api.Test;

import static cart.domain.OrderCalculator.HIGH_DISCOUNT_AMOUNT;
import static cart.domain.OrderCalculator.MID_DISCOUNT_AMOUNT;
import static common.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class OrderCalculatorTest {

    private OrderCalculator orderCalculator = new OrderCalculator();

    @Test
    void 주문을_받아서_총결제금액을_계산한다() {
        // given
        Order order = 주문_A_치킨_1.객체;
        long actual = 치킨.getPrice();

        // when
        long paymentAmount = orderCalculator.calculatePaymentAmount(order);

        // then
        assertThat(paymentAmount).isEqualTo(actual);
    }

    @Test
    void 결제금액이_3만원_이상이면_2천원_할인한다() {
        // given
        Order order = 주문_A_치킨_1_샐러드_1;
        long actual = 치킨.getPrice() + 샐러드.getPrice() - MID_DISCOUNT_AMOUNT;

        // when
        long paymentAmount = orderCalculator.calculatePaymentAmount(order);

        // then
        assertThat(paymentAmount).isEqualTo(actual);
    }

    @Test
    void 결제금액이_5만원_이상이면_5천원_할인한다() {
        // given
        Order order = 주문_A_치킨_1_샐러드_2;
        long actual = 치킨.getPrice() + 샐러드.getPrice() * 2L - HIGH_DISCOUNT_AMOUNT;

        // when
        long paymentAmount = orderCalculator.calculatePaymentAmount(order);

        // then
        assertThat(paymentAmount).isEqualTo(actual);
    }
}
