package cart.domain.coupon.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.domain.cart.CartItems;
import cart.exception.badrequest.BadRequestErrorType;
import cart.exception.badrequest.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

@DisplayName("퍼센트_쿠폰_정책")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PercentDiscountPolicyTest {

    private DiscountPolicy discountPolicy;

    @BeforeEach
    void setUp() {
        discountPolicy = new PercentDiscountPolicy();
    }

    @Nested
    class 쿠폰_할인_퍼센트_검증시_ {

        @ParameterizedTest
        @ValueSource(ints = {-10, 0})
        void 양수가_아니면_예외(int percent) {
            // when then
            assertThatThrownBy(() -> discountPolicy.validateValue(percent, 1000))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(BadRequestErrorType.DISCOUNT_PERCENT_INVALID.name());
        }

        @Test
        void _100보다_크면_예외() {
            // when then
            assertThatThrownBy(() -> discountPolicy.validateValue(110, 1000))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(BadRequestErrorType.DISCOUNT_PERCENT_INVALID.name());
        }

        @Test
        void 양수여야한다() {
            // when then
            assertThatNoException()
                    .isThrownBy(() -> discountPolicy.validateValue(90, 1000));
        }
    }

    @Test
    void 할인_금액이_반환된다() {
        // given
        CartItems cartItems = Mockito.mock(CartItems.class);
        given(cartItems.calculateTotalProductPrice()).willReturn(10000);

        // when
        int result = discountPolicy.calculateDiscountPrice(10, cartItems);

        // then
        assertThat(result).isEqualTo(1000);
    }
}
