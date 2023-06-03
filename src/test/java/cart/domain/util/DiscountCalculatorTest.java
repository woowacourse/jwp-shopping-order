package cart.domain.util;

import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.CouponFixture.COUPON_2_NOT_NULL_RATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cart.domain.Coupon;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiscountCalculatorTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateDiscountCalculator")
    @DisplayName("쿠폰을 적용한 뒤 가격을 계산한다.")
    void calculatePriceAfterDiscount(String testName, Integer originPrice, Coupon coupon, Integer expectedValue) {
        int result = DiscountCalculator.calculatePriceAfterDiscount(originPrice, coupon);

        assertThat(result).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> validateDiscountCalculator() {
        return Stream.of(
                Arguments.of("정액 할인 쿠폰", 10000, COUPON_1_NOT_NULL_PRICE, 5000),
                Arguments.of("할인율 쿠폰", 10000, COUPON_2_NOT_NULL_RATE, 9000)
        );
    }

}