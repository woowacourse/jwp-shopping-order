package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultDeliveryPolicyTest {

    @Test
    @DisplayName("calculateDeliveryFee()을 호출하면 3500의 값을 가진 Money가 반환된다.")
    void calculateDeliveryFee() {
        //given
        final DefaultDeliveryPolicy policy = new DefaultDeliveryPolicy("기본 배송 정책",
            Money.from(3_500));

        //when
        final Money deliveryFee = policy.calculateDeliveryFee(new Order(null, null, null));

        //then
        Assertions.assertThat(deliveryFee).isEqualTo(new Money(3500));
    }

}