package cart.domain.point;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static cart.fixture.MemberFixture.하디;
import static org.assertj.core.api.Assertions.assertThat;

public class PointTest {

    @Test
    void 포인트를_사용한다() {
        // given
        Timestamp current = new Timestamp(System.currentTimeMillis());
        Point point = new Point(1000L, 400L, 하디, current, current);

        // when
        point.usePoint(300L);

        // then
        assertThat(point.getLeftPoint()).isEqualTo(100L);
    }

    @Test
    void 남은포인트보다_많이_쓰려고할_경우_남은_포인트를_0_으로_만든다() {
        // given
        Timestamp current = new Timestamp(System.currentTimeMillis());
        Point point = new Point(1000L, 400L, 하디, current, current);

        // when
        point.usePoint(700L);

        // then
        assertThat(point.getLeftPoint()).isEqualTo(0L);
    }
}
