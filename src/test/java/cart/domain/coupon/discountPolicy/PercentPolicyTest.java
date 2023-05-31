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
class PercentPolicyTest {

    @Test
    void discount() {
        final DiscountPolicy percentPolicy = new PercentPolicy();

        final Money discount = percentPolicy.discount(new Money(5000L), 10);

        assertThat(discount.getValue()).isEqualTo(500L);
    }
}