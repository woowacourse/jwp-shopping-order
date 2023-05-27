package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeliveryFeeDiscountPolicyTest {

    @Test
    void 배달비_할인_가능_여부를_제외하고_0을_반환한다() {
        // given
        final DiscountPolicy percentDiscountPolicy = new DeliveryFeeDiscountPolicy();

        // expect
        assertAll(
                () -> assertThat(percentDiscountPolicy.getDiscountPercent()).isZero(),
                () -> assertThat(percentDiscountPolicy.getDiscountPrice()).isZero()
        );
    }

    @Test
    void 배달비_할인_여부를_true로_반환한다() {
        // given
        final DiscountPolicy percentDiscountPolicy = new DeliveryFeeDiscountPolicy();

        // expect
        assertThat(percentDiscountPolicy.isDiscountDeliveryFee()).isTrue();
    }

    @Test
    void 할인_정책_DELIVERY_타입을_반환한다() {
        // given
        final DiscountPolicy deliveryFeeDiscountPolicy = new DeliveryFeeDiscountPolicy();

        // expect
        assertThat(deliveryFeeDiscountPolicy.getDiscountPolicyType()).isEqualTo(DiscountPolicyType.DELIVERY);
    }
}
