package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CouponDiscountRateTest {

    @ParameterizedTest(name = "5~90 사이의 할인율이 들어오면 정상적으로 생성된다.")
    @ValueSource(ints = {5, 90})
    void create(final int discountRate) {
        assertDoesNotThrow(() -> CouponDiscountRate.create(discountRate));
    }

    @ParameterizedTest(name = "5 미만, 90 초과의 기간이 들어오면 예외가 발생한다.")
    @ValueSource(ints = {4, 91})
    void create_fail(final int discountRate) {
        assertThatThrownBy(() -> CouponDiscountRate.create(discountRate))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.COUPON_DISCOUNT_RATE_RANGE);
    }
}
