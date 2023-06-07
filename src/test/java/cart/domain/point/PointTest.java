package cart.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.product.Price;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PointTest {

    @Test
    void 포인트는_음수이면_예외를_발생한다() {
        // given
        BigDecimal point = BigDecimal.valueOf(-1);

        // expect
        assertThatThrownBy(() -> new Point(point))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("포인트는 최소 0 포인트 이상이어야합니다.");
    }

    @Test
    void 포인트는_다른_포인트를_받아_더할_수_있다() {
        // given
        final Point point = new Point(BigDecimal.valueOf(10));

        // when
        final Point result = point.add(new Point(BigDecimal.valueOf(30)));

        // then
        assertThat(result.getPoint().intValue()).isEqualTo(40L);
    }

    @Test
    void 포인트는_다른_포인트를_받아_뺄_수_있다() {
        // given
        final Point point = new Point(BigDecimal.valueOf(30));

        // when
        final Point result = point.subtract(new Point(BigDecimal.valueOf(10)));

        // then
        assertThat(result.getPoint().intValue()).isEqualTo(20);
    }

    @Test
    void 포인트는_다른_포인트보다_더_큰지_검사할_수_있다() {
        // given
        final Point point = new Point(BigDecimal.valueOf(30));

        // when
        final boolean result = point.isMoreThan(new Point(BigDecimal.valueOf(10)));

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 포인트는_돈을_받아_해당_돈의_포인트를_계산할_수_있다() {
        // given
        final Price price = new Price(BigDecimal.valueOf(10000));

        // when
        final Point result = Point.calculateFromPrice(price);

        // then
        assertThat(result.getPoint().intValue()).isEqualTo(250);
    }
}
