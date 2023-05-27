package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

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
        Percent percent = new Percent(value1);
        Price price = new Price(5000);

        assertThat(price.discount(percent)).isEqualTo(new Price(value2));
    }

    @Test
    @DisplayName("가격을 더할 수 있다.")
    void plus() {
        Price price1 = new Price(2000);
        Price price2 = new Price(1000);

        assertThat(price1.plus(price2)).isEqualTo(new Price(3000));
    }

    @Test
    @DisplayName("가격을 뺄 수 있다.")
    void minus() {
        Price price1 = new Price(2000);
        Price price2 = new Price(1000);

        assertThat(price1.minus(price2)).isEqualTo(new Price(1000));
    }

}