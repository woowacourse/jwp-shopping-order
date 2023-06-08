package cart.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.CouponRepository;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.FixDiscountPolicy;
import cart.coupon.domain.GeneralCouponType;
import cart.coupon.domain.RateDiscountPolicy;
import cart.coupon.domain.SpecificCouponType;
import cart.coupon.domain.TargetType;
import cart.coupon.presentation.dto.AllCouponQueryResponse;
import cart.coupon.presentation.dto.GeneralCouponResponse;
import cart.coupon.presentation.dto.SpecificCouponResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponQueryService (은)는")
class CouponQueryServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponQueryService queryService;

    @Test
    void 특정_회원의_쿠폰을_조회한다() {
        // given
        List<Coupon> coupons = List.of(new Coupon(
                        1L,
                        "말랑이 멋진쿠폰",
                        new RateDiscountPolicy(50),
                        new GeneralCouponType(),
                        1L),
                new Coupon(
                        2L,
                        "코코닥 멋진쿠폰",
                        new FixDiscountPolicy(5000000),
                        new SpecificCouponType(1L),
                        1L)
        );
        given(couponRepository.findAllByMemberId(1L))
                .willReturn(coupons);

        // when
        AllCouponQueryResponse actual = queryService.findAllByMemberId(1L);

        // then
        AllCouponQueryResponse expected = new AllCouponQueryResponse(
                List.of(new GeneralCouponResponse(
                        1L,
                        "말랑이 멋진쿠폰",
                        1L,
                        DiscountType.RATE,
                        TargetType.ALL,
                        50
                )),
                List.of(new SpecificCouponResponse(
                        2L,
                        "코코닥 멋진쿠폰",
                        1L,
                        DiscountType.FIX,
                        TargetType.SPECIFIC,
                        5000000,
                        1L
                ))
        );
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}