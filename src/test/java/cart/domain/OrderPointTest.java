package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class OrderPointTest {

    @Test
    void construct() {
        final Point usedPoint = Point.valueOf(10);
        final Point earnedPoint = Point.valueOf(3);
        final OrderPoint orderPoint = new OrderPoint(1L, usedPoint, earnedPoint);
        assertAll(
                () -> assertThat(orderPoint.getPointId()).isEqualTo(1L),
                () -> assertThat(orderPoint.getUsedPoint()).isEqualTo(Point.valueOf(10)),
                () -> assertThat(orderPoint.getEarnedPoint()).isEqualTo(Point.valueOf(3))
        );
    }
}
