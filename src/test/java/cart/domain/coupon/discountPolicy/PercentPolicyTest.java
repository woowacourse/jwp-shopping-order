package cart.domain.coupon.discountPolicy;

import cart.domain.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PercentPolicyTest {

    @Test
    void discount() {
        final DiscountPolicy percentPolicy = new PercentPolicy();

        final Money discount = percentPolicy.discount(new Money(5000L), BigDecimal.valueOf(10));

        assertThat(discount.getValue().longValue()).isEqualTo(500L);
    }
}