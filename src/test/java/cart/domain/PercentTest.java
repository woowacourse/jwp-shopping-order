package cart.domain;

import cart.domain.value.Percent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PercentTest {
    @ParameterizedTest
    @DisplayName("잘못된 퍼센트 값인경우 예외가 발생한다.")
    @ValueSource(ints = {-1, 101, 1000})
    void exception(int percent) {
        assertThatThrownBy(() -> new Percent(percent))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("올바른 퍼센트 값인 경우")
    @CsvSource(value = {"0,0.0", "20,0.2", "15,0.15"})
    void valid(int percent, double percentage) {
        double result = new Percent(percent).getPercentage();

        assertThat(result).isEqualTo(percentage);
    }

    @Test
    @DisplayName("0퍼센트 인 경우를 확인한다.")
    void isZero() {
        Percent percent = new Percent(0);

        assertThat(percent.isZero()).isTrue();
    }

}