package cart.domain;

import static cart.domain.Point.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NegativePointException;
import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void constructWithNegativeValueThrowException() {
        assertThatThrownBy(() -> valueOf(-1))
                .isInstanceOf(NegativePointException.class);
    }

    @Test
    void constructWithZero() {
        assertThatCode(() -> valueOf(0))
                .doesNotThrowAnyException();
    }

    @Test
    void isLessThanTrue() {
        final Point point = valueOf(1);
        assertThat(point.isLessThan(valueOf(2))).isTrue();
    }

    @Test
    void isLessThanFalse() {
        final Point point = valueOf(1);
        assertThat(point.isLessThan(valueOf(1))).isFalse();
    }

    @Test
    void subtract() {
        final Point point = valueOf(100);
        final Point result = point.subtract(valueOf(10));
        assertThat(result).isEqualTo(Point.valueOf(90));
    }

    @Test
    void subtractSame() {
        final Point point = valueOf(100);
        final Point result = point.subtract(valueOf(100));
        assertThat(result).isEqualTo(Point.ZERO);
    }

    @Test
    void subtractException() {
        final Point point = valueOf(100);
        assertThatThrownBy(() -> point.subtract(valueOf(101)))
                .isInstanceOf(NegativePointException.class);
    }
}
