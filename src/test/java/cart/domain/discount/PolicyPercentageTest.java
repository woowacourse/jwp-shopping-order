package cart.domain.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PolicyPercentageTest {

    @DisplayName("20% 할인 정책을 적용하면 전체 가격에서 20%를 빼준다.")
    @Test
    void discount_twenty_percent_when_apply_percentage_policy() {
        // given
        Policy policy = new PolicyPercentage(20);
        int total = 10000;

        // when
        int result = policy.calculate(total);

        // then
        assertThat(result).isEqualTo(8000);
    }
}
