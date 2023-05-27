package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DiscountPolicyTest {

    @Test
    void 정량으로_할인한다() {
        var policy = DiscountPolicy.FIXED;

        assertThat(policy.discount(new Money(11000), 5000)).isEqualTo(new Money(6000));
    }

    @Test
    void 백분율로_할인한다() {
        var policy = DiscountPolicy.PERCENTAGE;

        assertThat(policy.discount(new Money(11000), 50)).isEqualTo(new Money(5500));
    }

    @Test
    void 상품_가격_이상으로_할인하면_0원이_된다() {
        var policy = DiscountPolicy.FIXED;

        assertThat(policy.discount(new Money(100), 1000)).isEqualTo(Money.ZERO);
    }
}
