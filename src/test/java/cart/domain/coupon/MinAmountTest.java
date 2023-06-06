package cart.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MinAmountTest {

    @Test
    @DisplayName("최소 주문 금액에 음수가 오면 예외발생")
    void validateExceptionTest() {
        int minAmount = -1;
        assertThatThrownBy(() -> MinAmount.from(minAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최소 주문 금액은 음수가 될 수 없습니다.");
    }

    @Test
    @DisplayName("최소 주문 금액에 양수가 오면 정상수행")
    void validateTest() {
        int minAmount = 1000;
        assertDoesNotThrow(() -> MinAmount.from(minAmount));
    }
}
