package cart.service;

import static cart.fixture.TestFixture.쿠폰_1000원;
import static cart.fixture.TestFixture.쿠폰_10퍼센트;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.IssuableCoupon;
import cart.domain.coupon.repository.CouponRepository;
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
class CouponIssuerTest {

    @InjectMocks
    private CouponIssuer couponIssuer;

    @Mock
    private CouponRepository couponRepository;

    @Test
    void 결제_금액에_따라_쿠폰을_발급한다() {
        given(couponRepository.findAllIssuable())
                .willReturn(List.of(
                        new IssuableCoupon(1L, 쿠폰_10퍼센트, new Money(10000)),
                        new IssuableCoupon(2L, 쿠폰_1000원, new Money(2000))
                ));

        List<Coupon> coupons = couponIssuer.issueAllCoupons(new Money(3000));

        assertThat(coupons).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(쿠폰_1000원));
    }

    @Test
    void 쿠폰_발급_조건에_맞지_않으면_쿠폰을_발급하지_않는다() {
        given(couponRepository.findAllIssuable())
                .willReturn(List.of(
                        new IssuableCoupon(1L, 쿠폰_10퍼센트, new Money(10000)),
                        new IssuableCoupon(2L, 쿠폰_1000원, new Money(2000))
                ));

        List<Coupon> coupons = couponIssuer.issueAllCoupons(new Money(1000));

        assertThat(coupons).isEmpty();
    }
}
