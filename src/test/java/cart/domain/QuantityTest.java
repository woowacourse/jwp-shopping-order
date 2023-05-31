package cart.domain;

import cart.exception.NotEnoughQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Quantity 단위 테스트")
class QuantityTest {

    @Test
    @DisplayName("수량이 양수인 경우 객체 생성에 성공한다.")
    void construct_success_when_value_is_positive() {
        // given
        assertThatCode(() -> new Quantity(50))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("수량이 0인 경우 객체 생성에 성공한다.")
    void construct_success_when_value_is_zero() {
        // given
        assertThatCode(() -> new Quantity(0))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("수량이 음수인 경우 객체 생성에 실패한다.")
    void construct_success_when_value_is_negative() {
        // given
        assertThatThrownBy(() -> new Quantity(-10))
                .isInstanceOf(NotEnoughQuantityException.class);
    }

    @Test
    @DisplayName("뺄셈에 성공한다.")
    void subtract_success() {
        // given
        final Quantity quantity = new Quantity(50);
        final Quantity subtraction = new Quantity(10);

        // when
        final Quantity subtractedQuantity = quantity.subtract(subtraction);

        // then
        final Quantity expected = new Quantity(40);
        assertThat(subtractedQuantity).isEqualTo(expected);
    }

    @Test
    @DisplayName("뺄셈 결과가 음수인 경우 예외가 발생한다.")
    void subtract_fail_when_result_is_negative() {
        // given
        final Quantity quantity = new Quantity(10);
        final Quantity subtraction = new Quantity(20);

        // when, then
        assertThatThrownBy(() -> quantity.subtract(subtraction))
                .isInstanceOf(NotEnoughQuantityException.class);
    }
}
