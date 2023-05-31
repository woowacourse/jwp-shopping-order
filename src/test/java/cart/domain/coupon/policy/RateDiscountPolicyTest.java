package cart.domain.coupon.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RateDiscountPolicyTest {

    @Test
    void 정률정책은_해당률만큼_할인한다() {
        RateDiscountPolicy 정률정책 = new RateDiscountPolicy(10L);

        assertThat(정률정책.calculatePrice(10000L).intValue()).isEqualTo(9000);
    }
}
