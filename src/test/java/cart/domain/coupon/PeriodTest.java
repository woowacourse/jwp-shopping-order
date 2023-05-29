package cart.domain.coupon;

import cart.exception.GlobalException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PeriodTest {

    @ParameterizedTest(name = "유효 기간은 1일 이상, 365일 이하이다.")
    @ValueSource(ints = {0, 366})
    void createPriceTest1(int period) {
        assertThatThrownBy(() -> new Period(period))
                .isInstanceOf(GlobalException.class);
    }

    @ParameterizedTest(name = "유효 기간은 1일 이상, 365일 이하이다.")
    @ValueSource(ints = {1, 365})
    void createPriceTest2(int period) {
        assertDoesNotThrow(() -> new Period(period));
    }

}
