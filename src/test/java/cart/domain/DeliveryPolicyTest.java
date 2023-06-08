package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeliveryPolicyTest {
    @Test
    @DisplayName("기본 배송비 테스트")
    void defaultTest() {
        //given
        Order orderWithoutId = OrderFixture.orderWithoutId;

        //when
        DeliveryPolicy defaultDeliveryPolicy = DeliveryPolicy.DEFAULT;
        Money money = defaultDeliveryPolicy.calculateDeliveryFee(orderWithoutId);

        //then
        assertThat(money).isEqualTo(Money.from(3500));
    }
}