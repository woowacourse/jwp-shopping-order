package cart.service;

import static cart.fixture.TestFixture.밀리;
import static cart.fixture.TestFixture.밀리_인증_정보;
import static cart.fixture.TestFixture.밀리_쿠폰_1000원;
import static cart.fixture.TestFixture.밀리_쿠폰_10퍼센트;
import static cart.fixture.TestFixture.쿠폰_1000원;
import static cart.fixture.TestFixture.쿠폰_10퍼센트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.domain.Money;
import cart.domain.coupon.repository.MemberCouponRepository;
import cart.domain.repository.MemberRepository;
import cart.dto.MemberCouponsResponse;
import java.util.List;
import java.util.Optional;
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
    private CouponIssuer couponIssuer;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 멤버의_사용_가능한_모든_쿠폰을_조회한다() {
        given(memberCouponRepository.findNotExpired(밀리))
                .willReturn(List.of(
                        밀리_쿠폰_10퍼센트,
                        밀리_쿠폰_1000원
                ));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(밀리));

        MemberCouponsResponse couponsResponse = couponService.findAll(밀리_인증_정보);

        assertThat(couponsResponse.getFixedCoupon()).hasSize(1);
        assertThat(couponsResponse.getRateCoupon()).hasSize(1);
    }

    @Test
    void 결제_금액에_따라_쿠폰을_발급한다() {
        given(couponIssuer.issueAllCoupons(any()))
                .willReturn(List.of(
                        쿠폰_10퍼센트,
                        쿠폰_1000원
                ));

        couponService.issueByOrderPrice(new Money(3000), 밀리);

        verify(memberCouponRepository, times(1)).saveAll(any());
    }
}
