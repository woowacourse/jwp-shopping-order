package cart.domain;

import cart.exception.PointException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PointTest {

    @DisplayName("포인트는 음수일 수 없다")
    @Test
    void negativeException() {
        assertThatThrownBy(() -> new Point(-1)).isInstanceOf(PointException.NegativePoint.class);
    }

    @DisplayName("포인트를 증가시킨다")
    @Test
    void add() {
        final Point point = new Point(5);

        assertThat(point.add(5)).isEqualTo(new Point(10));
    }

    @DisplayName("포인트를 감소시킨다")
    @Test
    void subtract() {
        final Point point = new Point(5);

        assertThat(point.subtract(5)).isEqualTo(new Point(0));
    }
}
