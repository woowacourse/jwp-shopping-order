package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

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
}
