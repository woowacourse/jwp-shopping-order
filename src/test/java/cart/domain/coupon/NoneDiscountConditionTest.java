package cart.domain.coupon;

import static cart.domain.coupon.DiscountConditionType.NONE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NoneDiscountConditionTest {

    @Test
    void 할인_조건_NONE_타입을_반환한다() {
        // given
        final DiscountCondition noneDiscountCondition = new NoneDiscountCondition();

        // expect
        assertThat(noneDiscountCondition.getDiscountConditionType()).isEqualTo(NONE);
    }
}
