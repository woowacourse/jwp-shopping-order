package cart.domain;

import static cart.domain.fixture.DiscountPolicyFixture.defaultDiscountPolicy;
import static cart.domain.fixture.OrderFixture.order;
import static cart.domain.fixture.OrderFixture.orderUnderDiscountThreshold;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {


    @Test
    @DisplayName("Order를 인자로 받아 PaymentRecord를 생성한다.")
    void createPaymentRecord() {
        //given
        DiscountPolicy discountPolicy = defaultDiscountPolicy;
        DeliveryPolicy deliveryPolicy = new DefaultDeliveryPolicy();
        Payment payment = new Payment(List.of(discountPolicy), List.of(deliveryPolicy));
        PaymentRecord expected = new PaymentRecord(order, Money.from(55_000),
                Map.of(discountPolicy, Money.from(5_500)), Map.of(deliveryPolicy, Money.from(3_500)));
        //when
        PaymentRecord actual = payment.createPaymentRecord(order);
        //then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("적용대상이 아닌 주문은 할인하지 않는다. ")
    void createPaymentRecord_notAppliedDiscount() {
        //given
        DiscountPolicy discountPolicy = defaultDiscountPolicy;
        DeliveryPolicy deliveryPolicy = new DefaultDeliveryPolicy();
        Payment payment = new Payment(List.of(discountPolicy), List.of(deliveryPolicy));
        PaymentRecord expected = new PaymentRecord(orderUnderDiscountThreshold, Money.from(40_000),
                Map.of(), Map.of(deliveryPolicy, Money.from(3_500)));
        //when
        PaymentRecord actual = payment.createPaymentRecord(orderUnderDiscountThreshold);
        //then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

}