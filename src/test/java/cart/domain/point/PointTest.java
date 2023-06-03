package cart.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PointTest {

    @Test
    void 포인트는_음수이면_예외를_발생한다() {
        // given
        int point = -1;

        // expect
        assertThatThrownBy(() -> new Point(point))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트는 최소 0 포인트 이상이어야합니다.");
    }

    @Test
    void 포인트는_다른_포인트를_받아_더할_수_있다() {
        // given
        final Point point = new Point(10);

        // when
        final Point result = point.add(new Point(30));

        // then
        assertThat(result.getPoint()).isEqualTo(40);
    }

    @Test
    void 포인트는_다른_포인트를_받아_뺄_수_있다() {
        // given
        final Point point = new Point(30);

        // when
        final Point result = point.substract(new Point(10));

        // then
        assertThat(result.getPoint()).isEqualTo(20);
    }
}
