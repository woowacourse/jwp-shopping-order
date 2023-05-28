package cart.domain.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PolicyDiscountTest {

    @DisplayName("1000원 할인 정책을 적용하면 전체 금액에서 1000원을 빼고 반환해준다.")
    @Test
    void discount_10_k_won_when_apply_discount_policy() {
        // given
        Policy policy = new PolicyDiscount(1000);
        int total = 10000;

        // when
        int result = policy.calculate(total);

        // then
        assertThat(result).isEqualTo(9000);
    }
}
