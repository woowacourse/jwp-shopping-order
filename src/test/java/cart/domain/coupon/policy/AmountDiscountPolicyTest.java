package cart.domain.coupon.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.domain.cartitem.CartItems;
import cart.exception.StoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

@DisplayName("금액_쿠폰_정책")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AmountDiscountPolicyTest {

    private DiscountPolicy discountPolicy;

    @BeforeEach
    void setUp() {
        discountPolicy = new AmountDiscountPolicy();
    }

    @Nested
    class 쿠폰_할인_가격_검증시_ {

        @ParameterizedTest
        @ValueSource(ints = {-1000, 0})
        void 양수가_아니면_예외(int discountAmount) {
            // when then
            assertThatThrownBy(() -> discountPolicy.validateValue(discountAmount, 1000))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("할인 금액은 0원보다 커야합니다.");
        }

        @Test
        void 최소_주문_금액이_할인금액보다_작으면_예외() {
            // given
            int discountAmount = 1000;
            int minOrderPrice = 900;

            // when then
            assertThatThrownBy(() -> discountPolicy.validateValue(discountAmount, minOrderPrice))
                    .isInstanceOf(StoreException.class)
                    .hasMessage("최소 주문 금액은 할인 금액보다 크거나 같아야합니다.");
        }

        @ParameterizedTest
        @CsvSource(value = {"1000:1000", "1000:15000"}, delimiter = ':')
        void 크거나_같아야한다(int discountAmount, int minOrderPrice) {
            // when then
            assertThatNoException()
                    .isThrownBy(() -> discountPolicy.validateValue(discountAmount, minOrderPrice));
        }
    }

    @ParameterizedTest(name = "쿠폰 할인 금액: {0}원 / 물건 가격: {1} => 최종 할인 가격: {2}")
    @CsvSource(value = {"1000:2000:1000", "2000:1000:1000", "1000:1000:1000"}, delimiter = ':')
    void 할인_금액_계산시_쿠폰의_할인금액과_총가격중_작은_값이_반환(int couponDiscountAmount, int totalPrice, int discountPrice) {
        // given
        CartItems cartItems = Mockito.mock(CartItems.class);
        given(cartItems.getTotalPrice()).willReturn(totalPrice);

        // when
        int result = discountPolicy.calculateDiscountPrice(couponDiscountAmount, cartItems);

        // then
        assertThat(result).isEqualTo(discountPrice);
    }
}
