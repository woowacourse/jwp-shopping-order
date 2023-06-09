package cart.domain;

import static cart.domain.fixture.PaymentRecordFixture.PAYMENT;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    @DisplayName("총 할인금액을 반환한다.")
    void calculateDiscountedAmount() {
        //given
        //when
        Money discountedAmount = PAYMENT.calculateDiscountedPrice();
        //then
        Assertions.assertThat(discountedAmount).isEqualTo(new Money(49_500));

    }


    @Test
    @DisplayName("배송 금액을 반환한다.")
    void calculateDeliveryFee() {
        //given
        //when
        Money deliveryAmount = PAYMENT.calculateDeliveryFee();
        //then
        Assertions.assertThat(deliveryAmount).isEqualTo(new Money(3_500));
    }

    @Test
    @DisplayName("최종 결제 금액을 반환한다.")
    void createFinalPaymentAmount() {
        //given
        //when
        Money finalPaymentAmount = PAYMENT.calculateFinalPrice();
        //then
        Assertions.assertThat(finalPaymentAmount).isEqualTo(new Money(53_000));
    }
}