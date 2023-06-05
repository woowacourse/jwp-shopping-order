package cart.domain.coupon.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PriceDiscountPolicyTest {

    @Test
    void 정액정책은_해당가격만큼_할인한다() {
        PriceDiscountPolicy 정액정책 = new PriceDiscountPolicy(1000L);

        assertThat(정액정책.calculatePrice(10000L).intValue()).isEqualTo(9000);
    }
}
