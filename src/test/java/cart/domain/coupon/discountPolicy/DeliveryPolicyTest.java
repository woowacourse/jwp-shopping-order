package cart.domain.coupon.discountPolicy;

import cart.domain.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DeliveryPolicyTest {

    @Test
    void 배달비를_전액_할인한다() {
        // given
        final DiscountPolicy deliveryPolicy = new DeliveryPolicy();

        // when
        final Money discount = deliveryPolicy.discount(new Money(3000L), 0);

        // then
        assertThat(discount.getValue()).isEqualTo(3000L);
    }
}