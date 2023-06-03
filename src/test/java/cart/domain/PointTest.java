package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalPointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PointTest {

    @Test
    @DisplayName("0보다 작은 포인트는 생성할 수 없다.")
    void createPoint_negative_fail() {
        // when, then
        assertThatThrownBy(() -> new Point(-1))
            .isInstanceOf(IllegalPointException.class);
    }

    @Test
    @DisplayName("금액을 포인트 적립 방식에 맞춰 포인트로 변환할 수 있다.")
    void fromMoney() {
        // given
        Money money = Money.from(1000);

        // when
        Point point = Point.fromMoney(money);

        // then
        Point expected = new Point(25);
        assertThat(point).isEqualTo(expected);
    }

    @Test
    @DisplayName("포인트를 포인트 사용 방식에 맞춰 금액으로 변환할 수 있다.")
    void toMoney() {
        // given
        Point point = new Point(500);

        // when
        Money money = point.toMoney();

        // then
        assertThat(money).isEqualTo(Money.from(500));
    }

    @ParameterizedTest
    @CsvSource(value = {"501:true", "500:false"}, delimiter = ':')
    @DisplayName("포인트가 다른 포인트보다 큼을 확인할 수 있다.")
    void isGreaterThan(int standardValue, boolean expected) {
        // given
        Point standard = new Point(standardValue);
        Point other = new Point(500);

        // when
        boolean result = standard.isGreaterThan(other);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("포인트를 더할 수 있다.")
    void add() {
        // given
        Point standard = new Point(1000);
        Point other = new Point(500);

        // when
        Point result = standard.add(other);

        // then
        assertThat(result).isEqualTo(new Point(1500));
    }


    @ParameterizedTest
    @CsvSource(value = {"1000:0", "500:500"}, delimiter = ':')
    @DisplayName("포인트를 차감할 수 있다.")
    void subtract(int otherValue, int expectedValue) {
        // given
        Point standard = new Point(1000);
        Point other = new Point(otherValue);

        // when
        Point result = standard.subtract(other);

        // then
        assertThat(result).isEqualTo(new Point(expectedValue));
    }

    @Test
    @DisplayName("보유한 포인트보다 차감하려는 포인트가 더 크면 예외가 발생한다.")
    void subtract_fail() {
        // given
        Point standard = new Point(1000);
        Point other = new Point(1001);

        // when, then
        assertThatThrownBy(() -> standard.subtract(other))
            .isInstanceOf(IllegalPointException.class)
            .hasMessageContaining("보유한 포인트보다 큰 포인트를 차감할 수 없습니다.");
    }
}
