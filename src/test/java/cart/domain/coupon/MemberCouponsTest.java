package cart.domain.coupon;

import cart.domain.Money;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
class MemberCouponsTest {

    @Autowired
    private DiscountPolicyProvider discountPolicyProvider;

    @Test
    @DisplayName("쿠폰들 등록시 쿠폰 적용 순서에 맞춰 정렬한다")
    void coupon_order_test() {
        // given
        MemberCoupon amountCoupon = new MemberCoupon(AMOUNT_1000_COUPON);
        MemberCoupon rateCoupon = new MemberCoupon(RATE_10_COUPON);
        List<MemberCoupon> couponList = List.of(amountCoupon, rateCoupon);

        // when
        MemberCoupons memberCoupons = MemberCoupons.of(couponList, discountPolicyProvider);

        // then
        List<MemberCoupon> savedCoupons = memberCoupons.getMemberCoupons();
        assertThat(savedCoupons).containsExactly(rateCoupon, amountCoupon);
    }

    @Test
    @DisplayName("쿠폰들을 적용한다")
    void apply_coupons_test() {
        // given
        List<MemberCoupon> couponList = List.of(new MemberCoupon(AMOUNT_1000_COUPON), new MemberCoupon(RATE_10_COUPON));
        MemberCoupons coupons = MemberCoupons.of(couponList, discountPolicyProvider);

        // when
        Money actualPrice = coupons.apply(new Money(15000));

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualPrice).isEqualTo(new Money(12500));
            softly.assertThat(coupons.getMemberCoupons().get(0).getDiscountedPrice()).isEqualTo(new Money(1500));
            softly.assertThat(coupons.getMemberCoupons().get(0).isUsed()).isTrue();
            softly.assertThat(coupons.getMemberCoupons().get(1).getDiscountedPrice()).isEqualTo(new Money(1000));
            softly.assertThat(coupons.getMemberCoupons().get(1).isUsed()).isTrue();
        });
    }
}
