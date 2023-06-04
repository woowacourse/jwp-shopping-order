package cart.domain;

import static cart.domain.Point.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Timestamp;

import cart.exception.NegativePointException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @CsvSource({"2023-05-31 10:00:00,2023-11-30 10:00:00", "2023-06-01 10:00:00,2023-11-31 10:00:00"})
    @ParameterizedTest
    void getExpiredAt(final String created, final String expired) {
        final Timestamp createdAt = Timestamp.valueOf(created);
        final Timestamp result = Point.getExpiredAt(createdAt);
        assertThat(result).isEqualTo(Timestamp.valueOf(expired));
    }

    @Test
    void getEarningRate() {
        final double result = Point.getEarningRate();
        assertThat(result).isEqualTo(5);
    }

    @Test
    void getPoint() {
        final Price price = Price.valueOf(1000);
        final Point result = Point.getEarnedPoint(price);
        assertThat(result).isEqualTo(Point.valueOf(50));
    }

    @Test
    void getPointRoundDown() {
        final Price price = Price.valueOf(999);
        final Point result = Point.getEarnedPoint(price);
        assertThat(result).isEqualTo(Point.valueOf(49));
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
