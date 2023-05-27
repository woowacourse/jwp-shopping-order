package cart.domain.coupon;

import static cart.domain.coupon.DiscountConditionType.NONE;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NoneDiscountConditionTest {

    @Test
    void 할인_조건_NONE_타입을_반환한다() {
        // given
        final DiscountCondition discountCondition = new NoneDiscountCondition();

        // expect
        assertThat(discountCondition.getDiscountConditionType()).isEqualTo(NONE);
    }

    @Test
    void 조건에_만족하는지_확인한다() {
        // given
        final DiscountCondition discountCondition = new NoneDiscountCondition();

        // expect
        assertThat(discountCondition.isSatisfiedBy(Money.ZERO)).isTrue();
    }
}
