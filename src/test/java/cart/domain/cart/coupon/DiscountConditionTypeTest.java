package cart.domain.cart.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.coupon.DiscountConditionType;
import cart.exception.DiscountConditionNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DiscountConditionTypeTest {

    @Test
    void 입력받는_값에_해당하는_조건이_없으면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> DiscountConditionType.from("INVALID"))
                .isInstanceOf(DiscountConditionNotFoundException.class)
                .hasMessage("할인 조건을 찾을 수 없습니다.");
    }

    @Test
    void 이름에_해당하는_할인_조건을_반환한다() {
        // expect
        assertThat(DiscountConditionType.from("NONE")).isEqualTo(DiscountConditionType.NONE);
    }

}
