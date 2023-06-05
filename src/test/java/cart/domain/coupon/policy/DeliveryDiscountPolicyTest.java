package cart.domain.coupon.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DeliveryDiscountPolicyTest {

    @Test
    void 배달정책은_배달비를_할인한다() {
        DeliveryDiscountPolicy 배달정책 = new DeliveryDiscountPolicy();

        assertThat(배달정책.calculatePrice(10000L).intValue()).isEqualTo(7000);
    }
}
