package cart.domain.coupon;

import static cart.domain.coupon.DiscountConditionType.MINIMUM_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MinimumPriceDiscountConditionTest {

    @Test
    void 할인_조건_MINIMUM_PRICE_타입을_반환한다() {
        // given
        final DiscountCondition minimumPriceDiscountCondition = new MinimumPriceDiscountCondition(30000);

        // expect
        assertThat(minimumPriceDiscountCondition.getDiscountConditionType()).isEqualTo(MINIMUM_PRICE);
    }
}
