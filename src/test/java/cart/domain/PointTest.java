package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalPointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    @ValueSource(ints = {500, 499})
    @DisplayName("포인트가 다른 포인트보다 작거나 같음을 확인할 수 있다.")
    void isLessOrEqualThan(int standardValue) {
        // given
        Point standard = new Point(standardValue);
        Point other = new Point(500);

        // when
        boolean result = standard.isLessOrEqualThan(other);

        // then
        assertThat(result).isTrue();
    }
}
