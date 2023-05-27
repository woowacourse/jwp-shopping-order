package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AmountDiscountPolicyTest {

    @Test
    void 고정_금액을_제외하고_0이나_false를_반환한다() {
        // given
        final DiscountPolicy amountDiscountPolicy = new AmountDiscountPolicy(3000);

        // expect
        assertAll(
                () -> assertThat(amountDiscountPolicy.getDiscountPercent()).isZero(),
                () -> assertThat(amountDiscountPolicy.isDiscountDeliveryFee()).isFalse()
        );
    }

    @Test
    void 설정한_고정_금액을_그대로_반환한다() {
        // given
        final DiscountPolicy amountDiscountPolicy = new AmountDiscountPolicy(3000);

        // expect
        assertThat(amountDiscountPolicy.getDiscountPrice()).isEqualTo(3000);
    }

    @Test
    void 할인_정책_PRICE_타입을_반환한다() {
        // given
        final DiscountPolicy amountDiscountPolicy = new AmountDiscountPolicy(3000);

        // expect
        assertThat(amountDiscountPolicy.getDiscountPolicyType()).isEqualTo(DiscountPolicyType.PRICE);
    }
}
