package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cart.domain.cart.CartItems;
import cart.fixture.CouponFixture.금액_10000원이상_1000원할인;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberCouponTest {

    @Nested
    class 적용_가능여부_조회시 {

        @Test
        void 만료되었으면_거짓() {
            // given
            CartItems cartItems = new CartItems(Collections.emptyList());
            MemberCoupon memberCoupon = new MemberCoupon(1L, 금액_10000원이상_1000원할인.COUPON, false, LocalDateTime.MIN,
                    LocalDateTime.now());

            // when
            boolean applicable = memberCoupon.isApplicable(cartItems);

            // then
            assertThat(applicable).isFalse();
        }

        @Test
        void 이미_사용된_쿠폰이면_거짓() {
            // given
            CartItems cartItems = new CartItems(Collections.emptyList());
            MemberCoupon memberCoupon = new MemberCoupon(1L, 금액_10000원이상_1000원할인.COUPON, true, LocalDateTime.MAX,
                    LocalDateTime.now());

            // when
            boolean applicable = memberCoupon.isApplicable(cartItems);

            // then
            assertThat(applicable).isFalse();
        }

        @Test
        void 쿠폰_조건과_맞지않으면_거짓() {
            // given
            MemberCoupon memberCoupon = new MemberCoupon(1L, 금액_10000원이상_1000원할인.COUPON, false, LocalDateTime.MAX,
                    LocalDateTime.now());
            CartItems cartItems = Mockito.mock(CartItems.class);
            given(cartItems.getTotalProductPrice()).willReturn(2000);

            // then
            boolean applicable = memberCoupon.isApplicable(cartItems);

            // then
            assertThat(applicable).isFalse();
        }

        @Test
        void 조건을_만족하면_참() {
            // given
            MemberCoupon memberCoupon = new MemberCoupon(1L, 금액_10000원이상_1000원할인.COUPON, false, LocalDateTime.MAX,
                    LocalDateTime.now());
            CartItems cartItems = Mockito.mock(CartItems.class);
            given(cartItems.getTotalProductPrice()).willReturn(10000);

            // then
            boolean applicable = memberCoupon.isApplicable(cartItems);

            // then
            assertThat(applicable).isTrue();
        }
    }
}
