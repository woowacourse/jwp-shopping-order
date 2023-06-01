package cart.domain;

import cart.domain.point.SavePointPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class SavePointPolicyTest {

    private final SavePointPolicy savePointPolicy = new SavePointPolicy();

    @DisplayName("0원이상 50,000원 미만은 5%의 적립금을 받는다")
    @ParameterizedTest(name = "{0}원은 5%의 적립금을 받는다")
    @ValueSource(ints = {0, 5_000, 49_999})
    void calculateRange1(int payment) {
        // when
        double point = savePointPolicy.calculate(payment);
        System.out.println(point);

        // then
        assertThat(point).isEqualTo(Math.floor(payment * 0.05));
    }

    @DisplayName("50,000원 이상 100,00은 8%의 적립금을 받는다")
    @ParameterizedTest(name = "{0}원은 8%의 적립금을 받는다")
    @ValueSource(ints = {50_000, 85_000, 99_999})
    void calculateRange2(int payment) {
        // when
        double point = savePointPolicy.calculate(payment);

        // then
        assertThat(point).isEqualTo(Math.floor(payment * 0.08));
    }

    @DisplayName("100,00 이상은 10%의 적립금을 받는다")
    @ParameterizedTest(name = "{0}원은 10%의 적립금을 받는다")
    @ValueSource(ints = {100_000, 555_555, Integer.MAX_VALUE - 1})
    void calculateRange3(int payment) {
        // when
        double point = savePointPolicy.calculate(payment);

        // then
        assertThat(point).isEqualTo(Math.floor(payment * 0.1));
    }
}
