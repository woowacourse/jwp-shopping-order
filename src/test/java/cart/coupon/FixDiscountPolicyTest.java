package cart.coupon;

import static cart.coupon.exception.CouponExceptionType.INVALID_DISCOUNT_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.common.execption.BaseExceptionType;
import cart.coupon.domain.DiscountPolicy;
import cart.coupon.domain.FixDiscountPolicy;
import cart.coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("FixDiscountPolicy 은(는)")
class FixDiscountPolicyTest {

    @Test
    void 할인_금액은_양수여야한다() {
        // when
        BaseExceptionType baseExceptionType = assertThrows(CouponException.class, () ->
                new FixDiscountPolicy(0)
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(INVALID_DISCOUNT_AMOUNT);
    }

    @ParameterizedTest(name = "{0}원에 {1}원 고정 할인을 적용하면 {2}원이다")
    @CsvSource({
            "1000,500,500",
            "2000,1050,950",
            "10000,10000,0"
    })
    void 고정_금액을_할인한다(int before, int discountAmount, int after) {
        // given
        DiscountPolicy discountPolicy = new FixDiscountPolicy(discountAmount);

        // when
        int actual = discountPolicy.calculatePrice(before);

        // then
        assertThat(actual).isEqualTo(after);
    }

    @Test
    void 상품_가격보다_할인_금액이_더_크면_무료이다() {
        // given
        DiscountPolicy discountPolicy = new FixDiscountPolicy(1000);

        // when
        int actual = discountPolicy.calculatePrice(500);

        // then
        assertThat(actual).isEqualTo(0);
    }
}
