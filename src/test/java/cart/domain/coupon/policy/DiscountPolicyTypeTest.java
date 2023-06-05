package cart.domain.coupon.policy;

import static cart.domain.coupon.policy.DiscountPolicyType.findDiscountPolicy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.DiscountPolicyNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DiscountPolicyTypeTest {

    @Test
    void 존재하지_않는_쿠폰정책은_예외를_던진다() {
        assertThatThrownBy(() -> findDiscountPolicy("존재하지않아요", 0L))
                .isInstanceOf(DiscountPolicyNotFoundException.class)
                .hasMessage("쿠폰타입을 찾을 수 없습니다.");
    }
}
