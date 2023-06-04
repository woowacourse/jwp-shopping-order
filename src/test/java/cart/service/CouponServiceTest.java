package cart.service;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.밀리_쿠폰_1000원;
import static cart.fixture.TestFixture.밀리_쿠폰_10퍼센트;
import static cart.fixture.TestFixture.쿠폰_1000원;
import static cart.fixture.TestFixture.쿠폰_10퍼센트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.domain.Money;
import cart.domain.coupon.IssuableCoupon;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    @Test
    void 멤버의_사용_가능한_모든_쿠폰을_조회한다() {
        given(memberCouponRepository.findNotExpiredAllByMember(밀리))
                .willReturn(List.of(
                        밀리_쿠폰_10퍼센트,
                        밀리_쿠폰_1000원
                ));

        CouponResponse couponResponse = couponService.findAllByMember(밀리);

        assertThat(couponResponse.getFixedCoupon()).hasSize(1);
        assertThat(couponResponse.getRateCoupon()).hasSize(1);
    }

    @Test
    void 결제_금액에_따라_쿠폰을_발급한다() {
        given(couponRepository.findAllIssuable())
                .willReturn(List.of(
                        new IssuableCoupon(1L, 쿠폰_10퍼센트, new Money(10000)),
                        new IssuableCoupon(2L, 쿠폰_1000원, new Money(2000))
                ));

        couponService.issueByOrderPrice(new Money(3000), 밀리);

        verify(memberCouponRepository, times(1)).saveAll(any());
    }

    @Test
    void 쿠폰_발급_조건에_맞지_않으면_쿠폰을_발급하지_않는다() {
        given(couponRepository.findAllIssuable())
                .willReturn(List.of(
                        new IssuableCoupon(1L, 쿠폰_10퍼센트, new Money(10000)),
                        new IssuableCoupon(2L, 쿠폰_1000원, new Money(2000))
                ));

        couponService.issueByOrderPrice(new Money(1000), 밀리);

        verify(memberCouponRepository, never()).saveAll(any());
    }
}
