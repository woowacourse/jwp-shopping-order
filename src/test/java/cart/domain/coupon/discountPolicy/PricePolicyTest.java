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
class PricePolicyTest {

    @Test
    void 주어진_금액을_정가_할인한다() {
        // given
        final DiscountPolicy pricePolicy = new PricePolicy();

        // when
        final Money discount = pricePolicy.discount(new Money(5000L), 2000L);

        // then
        assertThat(discount.getValue()).isEqualTo(2000L);
    }
}