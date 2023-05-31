package cart.coupon.domain;

import static cart.coupon.exception.CouponExceptionType.INVALID_DISCOUNT_RATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.common.execption.BaseExceptionType;
import cart.coupon.exception.CouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("RateDiscountPolicy 은(는)")
class RateDiscountPolicyTest {

    @ParameterizedTest(name = "비율이 {0}%라면 예외")
    @ValueSource(ints = {101, 0})
    void 비율은_0_초과이며_100이하여야_한다(int rate) {
        // when
        BaseExceptionType baseExceptionType = assertThrows(CouponException.class, () ->
                new RateDiscountPolicy(rate)
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(INVALID_DISCOUNT_RATE);
    }

    @ParameterizedTest(name = "{0}원에 {1}% 비율 할인을 적용하면 {2}원이다")
    @CsvSource({
            "1000,50,500",
            "2000,55,900",
            "10000,33,6700",
            "10001,33,6701",
            "10002,33,6702",
            "10003,33,6703"
    })
    void 비율_금액을_할인한다(int before, int discountRate, int after) {
        // given
        DiscountPolicy discountPolicy = new RateDiscountPolicy(discountRate);

        // when
        int actual = discountPolicy.calculatePrice(before);

        // then
        assertThat(actual).isEqualTo(after);
    }
}
