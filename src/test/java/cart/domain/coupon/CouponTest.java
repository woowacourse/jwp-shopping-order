package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cart.domain.cart.CartItems;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CouponTest {

    @ParameterizedTest(name = "최소 주문 금액: {0}, 상품 총액: {1} => 쿠폰 적용 가능여부: {2}")
    @CsvSource(value = {"1000:1000:true", "1000:2000:true", "2000:1000:false"}, delimiter = ':')
    void 쿠폰_최소_주문_금액이_카트_아이템의_총_가격보다_크거나_같아야한다(int minOrderPrice, int totalPrice, boolean applicable) {
        // given
        // TODO: 실제 가격으로 대체
        CartItems cartItems = Mockito.mock(CartItems.class);
        given(cartItems.calculateTotalProductPrice()).willReturn(totalPrice);
        CouponInfo couponInfo = new CouponInfo("1000원할인", minOrderPrice, 2000);
        Coupon coupon = new Coupon(couponInfo, 1000, CouponType.FIXED_AMOUNT);

        // when
        boolean result = coupon.isApplicable(cartItems);

        // then
        assertThat(result).isEqualTo(applicable);
    }
}
