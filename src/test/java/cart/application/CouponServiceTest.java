package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.MemberCoupon;
import cart.dto.response.MemberCouponResponse;
import cart.repository.CouponRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponService 은(는)")
class CouponServiceTest {

    private CouponRepository couponRepository = mock(CouponRepository.class);
    private CouponService couponService = new CouponService(couponRepository);

    @Test
    void 회원_아이디를_통해_모든_쿠폰을_반환한다() {
        // given
        Coupon firstCoupon = new Coupon(1L, "1000원 쿠폰", CouponType.PRICE, 1000);
        Coupon secondCoupon = new Coupon(2L, "10% 쿠폰", CouponType.RATE, 10);
        List<MemberCoupon> memberCoupons = List.of(
                new MemberCoupon(1L, 1L, firstCoupon, false),
                new MemberCoupon(2L, 1L, secondCoupon, false));
        given(couponRepository.findAllByMemberId(1L))
                .willReturn(memberCoupons);

        // when
        List<MemberCouponResponse> actual = couponService.findAllByMemberId(1L);

        // then
        assertThat(actual.size()).isEqualTo(2);
        assertCoupon(actual.get(0), firstCoupon);
        assertCoupon(actual.get(1), secondCoupon);
    }

    private void assertCoupon(MemberCouponResponse actual, Coupon expected) {
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getDiscount().getType()).isEqualTo(expected.getType().name()),
                () -> assertThat(actual.getDiscount().getAmount()).isEqualTo(expected.getDiscountAmount())
        );
    }
}
