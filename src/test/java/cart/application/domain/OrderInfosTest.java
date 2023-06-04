package cart.application.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderInfosTest {

    @Test
    @DisplayName("모든 사용가능한 포인트의 합을 계산할 수 있다")
    void calculateAvailablePoint() {
        // given
        Product pointAvailable = new Product(0L, "", 1000, "", 10.0, true);
        Product pointUnavailable = new Product(0L, "", 500, "", 5.0, false);
        OrderInfos orderInfos = new OrderInfos(List.of(
                new OrderInfo(0L, pointAvailable, "", 0, "", 4),
                new OrderInfo(0L, pointUnavailable, "", 0, "", 4),
                new OrderInfo(0L, pointAvailable, "", 0, "", 4)
        ));
        // when
        long point = orderInfos.calculateAvailablePoint();
        // then
        assertThat(point).isEqualTo(8000L);
    }

    @Test
    @DisplayName("적립될 포인트의 합을 계산할 수 있다")
    void calculateEarnedPoint() {
        // given
        Product pointAvailable = new Product(0L, "", 1000, "", 10.0, true);
        Product pointUnavailable = new Product(0L, "", 500, "", 5.0, false);
        OrderInfos orderInfos = new OrderInfos(List.of(
                new OrderInfo(0L, pointAvailable, "", 0, "", 4),
                new OrderInfo(0L, pointUnavailable, "", 0, "", 4),
                new OrderInfo(0L, pointAvailable, "", 0, "", 4)
        ));
        // when
        long point = orderInfos.calculateEarnedPoint();
        // then
        assertThat(point).isEqualTo(900L);
    }
}
