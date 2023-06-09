package cart.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointTest {

    @Test
    void 포인트는_음수일_수_없다(){
        assertThatThrownBy(
                () -> new Point(BigDecimal.valueOf(-50))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("포인트는 0 이상이어야 합니다");
    }
    @Test
    void 포인트를_추가할_수_있다() {
        final Point point = new Point(BigDecimal.valueOf(50));
        final Point newPoint = point.addPoint(new Point(BigDecimal.valueOf(50)));
        assertThat(newPoint.getPoint().longValue()).isEqualTo(BigDecimal.valueOf(100).longValue());
    }

    @Test
    void 포인트를_차감할_수_있다() {
        final Point point = new Point(BigDecimal.valueOf(50));
        final Point newPoint = point.subtractPoint(new Point(BigDecimal.valueOf(50)));
        assertThat(newPoint.getPoint().longValue()).isEqualTo(BigDecimal.valueOf(0).longValue());
    }

    @Test
    void 포인트_차감액이_0_미만이면_예외가_발생한다() {
        final Point point = new Point(BigDecimal.valueOf(50));
        assertThatThrownBy(
                () -> point.subtractPoint(new Point(BigDecimal.valueOf(60)))
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("포인트는 0 이상이어야 합니다");
    }
}