package coupon.domain;

import static org.assertj.core.api.Assertions.assertThat;

import coupon.domain.discountcondition.DiscountCondition;
import coupon.domain.discountpolicy.RateDiscountPolicy;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CouponsTest {

    private static final Coupon 언제나_10_할인 = new Coupon("쿠폰1",
            DiscountCondition.AlwaysDiscountCondition,
            new RateDiscountPolicy(10));
    private static final Coupon 언제나_20_할인 = new Coupon("쿠폰2",
            DiscountCondition.NoneDiscountCondition,
            new RateDiscountPolicy(20));
    private static final Coupon 언제나_30_할인 = new Coupon("쿠폰3",
            DiscountCondition.AlwaysDiscountCondition,
            new RateDiscountPolicy(30));
    private static final Coupon 언제나_100_할인 = new Coupon("쿠폰4",
            DiscountCondition.AlwaysDiscountCondition,
            new RateDiscountPolicy(100));

    @Test
    void 할인_금액이_정상적으로_계산된다() {
        // given
        Coupons coupons = new Coupons(List.of(언제나_10_할인, 언제나_20_할인, 언제나_30_할인));

        // when
        Money result = coupons.calculateActualPrice(new Order(10000));

        // then
        assertThat(result).isEqualTo(new Money(6000));
    }

    @Test
    void 할인_금액이_총_금액을_넘어가면_0원이_된다() {
        // given
        Coupons coupons = new Coupons(List.of(언제나_10_할인, 언제나_20_할인, 언제나_30_할인, 언제나_100_할인));

        // when
        Money result = coupons.calculateActualPrice(new Order(10000));

        // then
        assertThat(result).isEqualTo(Money.ZERO);
    }
}
