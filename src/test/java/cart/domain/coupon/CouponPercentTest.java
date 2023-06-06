package cart.domain.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CouponPercentTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    @DisplayName("할인률에 음수, 100초과되는 수가 오면 예외발생")
    void validateExceptionTest(int percent) {
        assertThatThrownBy(() -> CouponPercent.from(percent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("할일률은 음수 혹은 100을 넘길 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, 99})
    @DisplayName("할인률에 0 ~ 100이 오면 정상수행")
    void validateTest(int percent) {
        assertDoesNotThrow(() -> CouponPercent.from(percent));
    }
}
