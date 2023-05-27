package cart.domain.cart.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.coupon.DiscountPolicyType;
import cart.exception.DiscountPolicyNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DiscountPolicyTypeTest {

    @Test
    void 입력받는_값에_해당하는_정책이_없으면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> DiscountPolicyType.from("INVALID"))
                .isInstanceOf(DiscountPolicyNotFoundException.class)
                .hasMessage("할인 정책을 찾을 수 없습니다.");
    }

    @Test
    void 이름에_해당하는_할인_정책을_반환한다() {
        // expect
        assertThat(DiscountPolicyType.from("PRICE")).isEqualTo(DiscountPolicyType.PRICE);
    }
}
