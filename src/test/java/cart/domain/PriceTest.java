package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {
    @Test
    @DisplayName("가격이 음수인 경우 예외가 발생한다.")
    void exception() {
        assertThatThrownBy(() -> new Price(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("퍼센트로 가격을 할인 할 수 있다.")
    @CsvSource(value = {"10,4500", "15,4250"})
    void discount(int value1, int value2) {
        Percentage percentage = new Percentage(value1);
        Price price = new Price(5000);

        assertThat(price.discount(percentage)).isEqualTo(new Price(value2));

    }

}