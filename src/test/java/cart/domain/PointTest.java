package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointTest {

    private static final Point testPoint = new Point(List.of(
            new PointHistory(1L, 0, 5000),
            new PointHistory(1L, 1000, 500),
            new PointHistory(1L, 3000, 1000)
    ));

    @Test
    @DisplayName("포인트 이력을 계산한다.")
    void calculateTotalPoint() {
        assertThat(testPoint.calculateTotalPoint()).isEqualTo(2500);
    }

    @Test
    @DisplayName("포인트 이력을 조회한다.")
    void getPointHistories() {
        assertThat(testPoint.getPointHistories()).usingRecursiveComparison().isEqualTo(List.of(
                new PointHistory(1L, 0, 5000),
                new PointHistory(1L, 1000, 500),
                new PointHistory(1L, 3000, 1000)));
    }

}
