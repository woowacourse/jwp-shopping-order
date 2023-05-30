package cart.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {

    private static final int DEFAULT_POINT = 100;

    @DisplayName("최소 포인트는 0 이상이다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void validPointTest(final int value) {
        final Point point = new Point(value);
        assertThat(point.getValue()).isEqualTo(value);
    }

    @DisplayName("포인트가 0 미만일 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    void invalidPointTest(final int value) {
        assertThrows(IllegalArgumentException.class, () -> new Point(value));
    }

    @DisplayName("보유중인 포인트보다 사용하려는 포인트가 큰 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {101, 102, 103})
    void usePointOverTest(final int value) {
        final Point point = new Point(DEFAULT_POINT);
        assertThrows(IllegalArgumentException.class, () -> point.usePoint(value));
    }

    @DisplayName("사용하려는 포인트가 음수이 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    void usePointUnderTest(final int value) {
        final Point point = new Point(DEFAULT_POINT);
        assertThrows(IllegalArgumentException.class, () -> point.usePoint(value));
    }

    @DisplayName("포인트를 사용하면 보유중인 포인트에서 차감된다.")
    @ParameterizedTest
    @CsvSource(value = {"0:100", "1:99", "50:50", "100:0"}, delimiter = ':')
    void usePointTest(final int usingPoint, final int remainPoint) {
        final Point point = new Point(DEFAULT_POINT);
        point.usePoint(usingPoint);
        assertThat(point.getValue()).isEqualTo(remainPoint);
    }

    @DisplayName("적립 포인트가 음수인 경우 Exception이 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    void savePointUnderTest(final int value) {
        final Point point = new Point(DEFAULT_POINT);
        assertThrows(IllegalArgumentException.class, () -> point.savePoint(value));
    }

    @DisplayName("포인트를 적립하면 보유중인 포인트에서 더해진다.")
    @ParameterizedTest
    @CsvSource(value = {"0:100", "1:101", "50:150", "100:200"}, delimiter = ':')
    void savePointTest(final int savingPoint, final int expectPoint) {
        final Point point = new Point(DEFAULT_POINT);
        point.savePoint(savingPoint);
        assertThat(point.getValue()).isEqualTo(expectPoint);
    }
}
