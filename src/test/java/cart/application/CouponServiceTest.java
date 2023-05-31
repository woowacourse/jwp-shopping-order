package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.dto.response.CouponResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponService 은(는)")
class CouponServiceTest {

    private CouponDao couponDao = mock(CouponDao.class);
    private CouponService couponService = new CouponService(couponDao);

    @Test
    void 회원_아이디를_통해_모든_쿠폰을_반환한다() {
        // given
        Coupon firstCoupon = new Coupon(1L, "1000원 쿠폰", CouponType.FIXED, 1000);
        Coupon secondCoupon = new Coupon(2L, "10% 쿠폰", CouponType.RATE, 10);
        List<Coupon> coupons = List.of(firstCoupon, secondCoupon);
        given(couponDao.findAllByMemberId(1L))
                .willReturn(coupons);

        // when
        List<CouponResponse> actual = couponService.findAllByMemberId(1L);

        // then
        assertThat(actual.size()).isEqualTo(2);
        assertCoupon(actual.get(0), firstCoupon);
        assertCoupon(actual.get(1), secondCoupon);
    }

    private void assertCoupon(CouponResponse actual, Coupon expected) {
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getType()).isEqualTo(expected.getType().name()),
                () -> assertThat(actual.getDiscountAmount()).isEqualTo(expected.getDiscountAmount())
        );
    }
}
