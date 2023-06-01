package cart.service;

import cart.controller.dto.CouponResponse;
import cart.controller.dto.CouponTypeResponse;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.service.coupon.CouponMapper;
import cart.service.coupon.CouponProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CouponProviderTest {

    private CouponProvider couponProvider;
    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    void init() {
        couponProvider = new CouponProvider(couponRepository, new CouponMapper());
    }

    @Test
    void 회원이_소유한_쿠폰을_조회한다() {
        // given
        final Coupon coupon1 = new Coupon(1L, "1000원 할인 쿠폰", "1000원이 할인 됩니다.", 1000, false);
        final Coupon coupon2 = new Coupon(2L, "2000원 할인 쿠폰", "2000원이 할인 됩니다.", 2000, false);
        final Coupon coupon3 = new Coupon(3L, "3000원 할인 쿠폰", "3000원이 할인 됩니다.", 3000, false);
        given(couponRepository.findCouponsByMemberId(anyLong())).willReturn(new Coupons(List.of(coupon1, coupon2, coupon3)));

        final Member member = new Member(1L, "a@a.com", "1234");

        // when
        final List<CouponResponse> coupons = couponProvider.findCouponByMember(member);

        // then
        assertAll(
                () -> assertThat(coupons).hasSize(3),
                () -> {
                    final List<Integer> discountAmounts = coupons.stream()
                            .map(CouponResponse::getDiscountAmount)
                            .collect(toList());
                    assertThat(discountAmounts).containsExactly(1000, 2000, 3000);
                }
        );
    }

    @Test
    void 전체_쿠폰을_조회한다() {
        // given
        final Coupon coupon1 = new Coupon(1L, "1000원 할인 쿠폰", "1000원이 할인 됩니다.", 1000, false);
        final Coupon coupon2 = new Coupon(2L, "2000원 할인 쿠폰", "2000원이 할인 됩니다.", 2000, false);
        final Coupon coupon3 = new Coupon(3L, "3000원 할인 쿠폰", "3000원이 할인 됩니다.", 3000, false);
        given(couponRepository.findCouponAll()).willReturn(new Coupons(List.of(coupon1, coupon2, coupon3)));

        // when
        final List<CouponTypeResponse> couponTypes = couponProvider.findCouponAll();

        // then
        assertAll(
                () -> assertThat(couponTypes).hasSize(3),
                () -> assertThat(couponTypes.stream()
                        .map(CouponTypeResponse::getDiscountAmount)
                        .collect(toList())).containsExactly(1000, 2000, 3000)
        );
    }
}
