package cart.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CouponAmountTest {

    @Test
    @DisplayName("할인액이 음수면 예외발생")
    void validateMinusExceptionTest() {
        int discountAmount = -1;
        int minAmount = 4000;
        assertThatThrownBy(() -> CouponAmount.of(discountAmount, minAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할인액은 음수가 올 수 없습니다.");
    }

    @Test
    @DisplayName("할인액이 최소 주문 금액 보다 크면 예외발생")
    void validateBiggerThanMinAmountExceptionTest() {
        int discountAmount = 5000;
        int minAmount = 4999;
        assertThatThrownBy(() -> CouponAmount.of(discountAmount, minAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할인액은 최소 주문 금액보다 클 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"2000, 5000", "3000, 15000"})
    @DisplayName("할인액이 음수가 아니고, 최소 주문 금액보다 크면 정상수행")
    void validateTest(int discountAmount, int minAmount) {
        assertDoesNotThrow(() -> CouponAmount.of(discountAmount, minAmount));
    }
}
