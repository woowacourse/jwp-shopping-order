package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PercentageTest {
    @ParameterizedTest
    @DisplayName("잘못된 퍼센트 값인경우 예외가 발생한다.")
    @ValueSource(ints = {-1, 0, 101, 1000})
    void exception(int percentage) {
        assertThatThrownBy(() -> new Percentage(percentage))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("올바른 퍼센트 값인 경우")
    @CsvSource(value = {"20,0.2", "15,0.15"})
    void valid(int value, double percentage) {
        double result = new Percentage(value).getPercentage();

        assertThat(result).isEqualTo(percentage);

    }

}