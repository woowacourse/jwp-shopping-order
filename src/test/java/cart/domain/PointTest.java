package cart.domain;

import cart.exception.OrderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void 사용한_포인트가_정책_상_사용할_수_없는_포인트이면_예외를_발생한다(final int usePoint) {
        Point point = new Point(1000);

        assertThatThrownBy(() -> point.validateUsablePoint(usePoint))
                .isInstanceOf(OrderException.IllegalUsePoint.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {3000, 3001})
    void 사용한_포인트가_정책_상_사용할_수_있는_포인트면_예외를_발생하지_않는다(final int usePoint) {
        Point point = new Point(6000);

        assertThatNoException().isThrownBy(
                () -> point.validateUsablePoint(usePoint)
        );
    }

    @Test
    void 총_상품_금액으로_포인트를_계산한다() {
        // given
        int totalProductPoint = 5_000;

        // when
        Point point = new Point(0);
        Point newPoint = point.calculatePointBy(totalProductPoint);

        // then
        assertThat(newPoint.getValue()).isEqualTo(250);
    }

    @Test
    void 포인트를_합한다() {
        // given
        Point point = new Point(3000);
        Point newPoint = new Point(10000);

        // when
        Point result = point.add(newPoint);

        // then
        assertThat(result.getValue()).isEqualTo(13000);
    }

    @Test
    void 포인트를_뺀다() {
        // given
        Point point = new Point(10000);
        Point otherPoint = new Point(3000);

        // when
        Point result = point.subtract(otherPoint);

        // then
        assertThat(result.getValue()).isEqualTo(7000);
    }

    @Test
    void 보유_포인트보다_사용_포인트가_많으면_예외를_발생한다() {
        // given
        Point point = new Point(1000);
        Point usePoint = new Point(10000);

        // expect
        assertThatThrownBy(() -> point.validateUsePoint(usePoint))
                .isInstanceOf(OrderException.IllegalUsePoint.class);
    }
}
