package cart.domain.coupon;

import static cart.domain.coupon.DiscountConditionType.MINIMUM_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MinimumPriceDiscountConditionTest {

    @Test
    void 할인_조건_MINIMUM_PRICE_타입을_반환한다() {
        // given
        final DiscountCondition discountCondition = new MinimumPriceDiscountCondition(30000);

        // expect
        assertThat(discountCondition.getDiscountConditionType()).isEqualTo(MINIMUM_PRICE);
    }

    @ParameterizedTest(name = "조건에 만족하는지 확인한다 최소금액: 30000 입력값: {0} 결과: {1}")
    @CsvSource(value = {"30000, true", "30001, true", "29999, false"})
    void 조건에_만족하는지_확인한다(final long price, final boolean result) {
        // given
        final DiscountCondition discountCondition = new MinimumPriceDiscountCondition(30000);

        // expect
        assertThat(discountCondition.isSatisfiedBy(Money.from(price))).isEqualTo(result);
    }

    @Test
    void 조건에_해당하는_최소_비용을_반환한다() {
        // given
        final DiscountCondition discountCondition = new MinimumPriceDiscountCondition(30000);

        // expect
        assertThat(discountCondition.getMinimumPrice()).isEqualTo(Money.from(30000));
    }
}
