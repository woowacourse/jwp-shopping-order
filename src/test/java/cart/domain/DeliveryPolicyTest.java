package cart.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("DeliveryPolicy 단위테스트")
class DeliveryPolicyTest {

    @Test
    void 총상품금액이_50000원_이상이면_배달비는_없다() {
        // given
        // when
        int deliveryFee = DeliveryPolicy.calculateDeliveryFee(55000);

        // then
        Assertions.assertThat(deliveryFee).isEqualTo(0);
    }

    @Test
    void 총상품금액이_50000원_미만이면_기본_배달비_3000원이_있다() {
        // given
        // when
        int deliveryFee = DeliveryPolicy.calculateDeliveryFee(10000);

        // then
        Assertions.assertThat(deliveryFee).isEqualTo(3000);
    }
}
