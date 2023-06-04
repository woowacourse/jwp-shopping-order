package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.IllegalUsePointException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PointTest {

    @Test
    void 값이_음수가_될_수_없다() {
        // given
        int value = -1;

        // expect
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Point(value)
        );
    }

    @Test
    void 최소_사용_포인트는_3_000_이다() {
        assertThat(Point.MIN_USAGE_VALUE).isEqualTo(3000);
    }

    @Test
    void 값이_같으면_같은_객체이다() {
        // given
        Point point = new Point(10);
        Point samePoint = new Point(10);

        // expect
        assertThat(point).isEqualTo(samePoint);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2999})
    void 사용한_포인트가_사용할_수_없는_포인트이면_예외를_발생한다(final int usePoint) {
        assertThatThrownBy(() -> Point.validateUsablePoint(usePoint))
                .isInstanceOf(IllegalUsePointException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3000, 3001})
    void 사용한_포인트가_사용할_수_있는_포인트면_예외를_발생하지_않는다(final int usePoint) {
        assertThatNoException().isThrownBy(
                () -> Point.validateUsablePoint(usePoint)
        );
    }
}
