package cart.service;

import cart.controller.dto.CouponResponse;
import cart.controller.dto.CouponTypeResponse;
import cart.domain.coupon.CouponRepository;
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
class CouponProviderTest extends CouponFixture {

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
        given(couponRepository.findCouponsByMemberId(anyLong())).willReturn(coupons);

        // when
        final List<CouponResponse> coupons = couponProvider.findCouponByMember(1L);

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
        given(couponRepository.findCouponAll()).willReturn(coupons);

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
