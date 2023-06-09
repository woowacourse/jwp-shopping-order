package cart.domain;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.CouponFixture.할인율_쿠폰;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CouponTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateCoupon")
    @DisplayName("쿠폰을 적용한 뒤 가격을 계산한다.")
    void calculatePriceAfterDiscount(String testName, Integer originPrice, Coupon coupon, Integer expectedValue) {
        Integer priceAfterDiscount = coupon.apply(originPrice);

        assertThat(priceAfterDiscount).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> validateCoupon() {
        return Stream.of(
                Arguments.of("정액 할인 쿠폰", 10000, 정액_할인_쿠폰, 5000),
                Arguments.of("할인율 쿠폰", 10000, 할인율_쿠폰, 9000)
        );
    }

}
