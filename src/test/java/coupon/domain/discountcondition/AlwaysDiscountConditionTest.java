package coupon.domain.discountcondition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class AlwaysDiscountConditionTest {

    @Test
    void 언제나_할인_조건을_만족한다() {
        // given
        DiscountCondition discountCondition = new AlwaysDiscountCondition();

        // when
        boolean result = discountCondition.isSatisfiedBy(null);

        // then
        assertThat(result).isTrue();
    }
}
