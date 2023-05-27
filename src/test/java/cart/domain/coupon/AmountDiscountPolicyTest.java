package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AmountDiscountPolicyTest {

    @Test
    void 고정_금액을_제외하고_0이나_false를_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new AmountDiscountPolicy(3000);

        // expect
        assertAll(
                () -> assertThat(discountPolicy.getDiscountPercent()).isZero(),
                () -> assertThat(discountPolicy.isDiscountDeliveryFee()).isFalse()
        );
    }

    @Test
    void 설정한_고정_금액을_그대로_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new AmountDiscountPolicy(3000);

        // expect
        assertThat(discountPolicy.getDiscountPrice()).isEqualTo(Money.from(3000));
    }

    @Test
    void 할인_정책_PRICE_타입을_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new AmountDiscountPolicy(3000);

        // expect
        assertThat(discountPolicy.getDiscountPolicyType()).isEqualTo(DiscountPolicyType.PRICE);
    }

    @Test
    void 고정된_금액을_할인하여_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new AmountDiscountPolicy(3000);

        // when
        final Money result = discountPolicy.calculatePrice(Money.from(30000));

        // then
        assertThat(result).isEqualTo(Money.from(27000));
    }

    @Test
    void 배달비는_할인하지_않고_그대로_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new AmountDiscountPolicy(3000);

        // when
        final Money result = discountPolicy.calculateDeliveryFee(Money.from(3000));

        // then
        assertThat(result).isEqualTo(Money.from(3000));
    }
}
