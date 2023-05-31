package cart.domain;

import cart.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Price 단위 테스트")
class PriceTest {

    @Test
    @DisplayName("금액이 양수인 경우 객체 생성에 성공한다.")
    void construct_success_when_amount_is_positive() {
        assertThatCode(() -> new Price(10000))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("금액이 0인 경우 객체 생성에 성공한다.")
    void construct_success_when_amount_is_zero() {
        assertThatCode(() -> new Price(0))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("금액이 음수인 경우 예외가 발생한다.")
    void construct_fail_when_amount_is_negative() {
        assertThatThrownBy(() -> new Price(-10000))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("3만원에서 2만원을 더하면 5만원이다.")
    void add_30000_and_20000_result_50000() {
        // given
        final Price price1 = new Price(30000);
        final Price price2 = new Price(20000);

        // when
        final Price addedPrice = price1.add(price2);

        // then
        final Price expected = new Price(50000);
        assertThat(addedPrice).isEqualTo(expected);
    }

    @Test
    @DisplayName("3만원에서 2만원을 빼면 1만원이다.")
    void subtract_30000_and_20000_result_10000() {
        // given
        final Price price1 = new Price(30000);
        final Price price2 = new Price(20000);

        // when
        final Price subtractedPrice = price1.subtract(price2);

        // then
        final Price expected = new Price(10000);
        assertThat(subtractedPrice).isEqualTo(expected);
    }

    @Test
    @DisplayName("뺄셈 결과가 음수인 경우 예외가 발생한다.")
    void subtract_fail_when_result_is_negative() {
        // given
        final Price price1 = new Price(20000);
        final Price price2 = new Price(30000);

        // when, then
        assertThatThrownBy(() -> price1.subtract(price2))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("3만원은 2만원보다 크거나 같다.")
    void isGreaterThanOrEqualTo_return_true_when_greater() {
        // given
        final Price price1 = new Price(30000);
        final Price price2 = new Price(20000);

        // when
        final boolean result = price1.isGreaterThanOrEqualTo(price2);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("3만원은 3만원보다 크거나 같다.")
    void isGreaterThanOrEqualTo_return_true_when_equal() {
        // given
        final Price price1 = new Price(30000);
        final Price price2 = new Price(30000);

        // when
        final boolean result = price1.isGreaterThanOrEqualTo(price2);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("2만원은 3만원보다 크거나 같지 않다.")
    void isGreaterThanOrEqualTo_return_false_when_less() {
        // given
        final Price price1 = new Price(20000);
        final Price price2 = new Price(30000);

        // when
        final boolean result = price1.isGreaterThanOrEqualTo(price2);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("2만원에 3을 곱하면 6만원이다.")
    void multiply_20000_and_3_result_60000() {
        // given
        final Price price = new Price(20000);
        final int count = 3;

        // when
        final Price result = price.multiply(count);

        // then
        final Price expected = new Price(60000);
        assertThat(result).isEqualTo(expected);
    }
}
