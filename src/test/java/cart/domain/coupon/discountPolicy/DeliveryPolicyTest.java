package cart.domain.coupon.discountPolicy;

import cart.domain.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DeliveryPolicyTest {

    @Test
    void 배달비를_전액_할인한다() {
        // given
        final DiscountPolicy deliveryPolicy = new DeliveryPolicy();

        // when
        final Money discount = deliveryPolicy.discount(new Money(3000L), BigDecimal.ZERO);

        // then
        assertThat(discount.getValue().longValue()).isEqualTo(3000L);
    }
}