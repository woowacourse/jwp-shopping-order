package cart.domain.coupon;

import cart.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static cart.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CouponTest {

    @DisplayName("Coupon이 정상적으로 생성된다.")
    @Test
    void coupon() {
        // when & then
        assertDoesNotThrow(() -> new Coupon("coupon", 10, 365, LocalDateTime.MAX));
    }

    @DisplayName("Coupon name이 1글자 미만 50글자 초과 시 INVALID_COUPON_NAME_LENGTH 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "123456789012345678901234567890123456789012345678901"})
    void coupon_InvalidNameLength(String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Coupon(invalidName, 10, 365, LocalDateTime.MAX))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_COUPON_NAME_LENGTH);
    }

    @DisplayName("Coupon discountRate이 5% 미만 90% 초과 시 INVALID_COUPON_DISCOUNT_RATE 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {4, 91})
    void coupon_InvalidDiscountRate(int invalidDiscountRate) {
        // when & then
        assertThatThrownBy(() -> new Coupon("coupon", invalidDiscountRate, 365, LocalDateTime.MAX))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_COUPON_DISCOUNT_RATE);
    }

    @DisplayName("Coupon period가 1일 미만 365일 초과 시 INVALID_COUPON_PERIOD_LENGTH 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 366})
    void coupon_InvalidPeriod(int invalidPeriod) {
        // when & then
        assertThatThrownBy(() -> new Coupon("coupon", 10, invalidPeriod, LocalDateTime.MAX))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_COUPON_PERIOD_LENGTH);
    }

    @DisplayName("Coupon ExpiredAt이 현재 시간 이전일 경우 INVALID_COUPON_EXPIRATION_DATE 예외가 발생한다.")
    @Test
    void coupon_InvalidExpiredAt() {
        // given
        LocalDateTime invalidExpiredAt = LocalDateTime.MIN;

        // when & then
        assertThatThrownBy(() -> new Coupon("coupon", 10, 365, invalidExpiredAt))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_COUPON_EXPIRATION_DATE);
    }
}
