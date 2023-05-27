package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.common.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeliveryFeeDiscountPolicyTest {

    @Test
    void 배달비_할인_가능_여부를_제외하고_0을_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new DeliveryFeeDiscountPolicy(true);

        // expect
        assertAll(
                () -> assertThat(discountPolicy.getDiscountPercent()).isZero(),
                () -> assertThat(discountPolicy.getDiscountPrice()).isEqualTo(Money.ZERO)
        );
    }

    @Test
    void 배달비_할인_여부를_true로_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new DeliveryFeeDiscountPolicy(true);

        // expect
        assertThat(discountPolicy.isDiscountDeliveryFee()).isTrue();
    }

    @Test
    void 할인_정책_DELIVERY_타입을_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new DeliveryFeeDiscountPolicy(true);

        // expect
        assertThat(discountPolicy.getDiscountPolicyType()).isEqualTo(DiscountPolicyType.DELIVERY);
    }

    @Test
    void 금액은_할인하지_않고_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new DeliveryFeeDiscountPolicy(true);

        // when
        final Money result = discountPolicy.calculatePrice(Money.from(30000));

        // then
        assertThat(result).isEqualTo(Money.from(30000));
    }

    @Test
    void 배달비를_전부_할인하여_반환한다() {
        // given
        final DiscountPolicy discountPolicy = new DeliveryFeeDiscountPolicy(true);

        // when
        final Money result = discountPolicy.calculateDeliveryFee(Money.from(3000));

        // then
        assertThat(result).isEqualTo(Money.from(0));
    }
}
