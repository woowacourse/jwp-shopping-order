package cart.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.NegativePointException;
import org.junit.jupiter.api.Test;

class PointTest {

    @Test
    void constructWithNegativeValueThrowException() {
        assertThatThrownBy(() -> Point.valueOf(-1))
                .isInstanceOf(NegativePointException.class);
    }

    @Test
    void constructWithZero() {
        assertThatCode(() -> Point.valueOf(0))
                .doesNotThrowAnyException();
    }
}
