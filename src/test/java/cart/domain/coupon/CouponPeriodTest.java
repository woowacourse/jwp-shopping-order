package cart.domain.coupon;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CouponPeriodTest {

    @ParameterizedTest(name = "1~365 사이의 기간이 들어오면 정상적으로 생성된다.")
    @ValueSource(ints = {1, 365})
    void create(final int period) {
        assertDoesNotThrow(() -> CouponPeriod.create(period));
    }

    @ParameterizedTest(name = "1 미만, 365 초과의 기간이 들어오면 예외가 발생한다.")
    @ValueSource(ints = {0, 366})
    void create_fail(final int period) {
        assertThatThrownBy(() -> CouponPeriod.create(period))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.COUPON_PERIOD_RANGE);
    }
}
