package cart.domain.coupon;

import cart.exception.GlobalException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DiscountRateTest {

    @ParameterizedTest(name = "쿠폰의 할인율은 5 ~ 90% 사이여야한다.")
    @ValueSource(ints = {4, 91})
    void createDiscountRateTest1(int rate) {
        assertThatThrownBy(() -> new DiscountRate(rate))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "쿠폰의 할인율은 5 ~ 90% 사이여야한다.")
    @ValueSource(ints = {5, 90})
    void createDiscountRateTest2(int rate) {
        assertDoesNotThrow(() -> new DiscountRate(rate));
    }
}
