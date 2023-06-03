package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PointHistoryTest {

    @ParameterizedTest
    @CsvSource({"1, 0, 10000,  10000", "1, 5000, 10000, 5000", "1, 100, 3000, 2900"})
    void calculatePoint(final Long orderId, final int earnedPoint, final int usedPoint, final int nowPoint) {
        PointHistory pointHistory = new PointHistory(orderId, earnedPoint, usedPoint);
        assertThat(pointHistory.calculatePoint()).isEqualTo(nowPoint);
    }

}
