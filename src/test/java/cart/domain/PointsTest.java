package cart.domain;

import cart.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointsTest {

    @DisplayName("포인트의 금액을 모두 합할 수 있다.")
    @Test
    void getTotalPoints() {
        Points points = new Points(List.of(Point.of(100, "테스트용 포인트", LocalDate.now()),
                Point.of(10000, "테스트용 포인트", LocalDate.now())));

        assertThat(points.getTotalPoints()).isEqualTo(10100);
    }

    @DisplayName("유효기간이 빠른 순으로 사용할 포인트들을 구할 수 있다.(포인트가 딱 떨어지지 않을 때)")
    @Test
    void getUsePoints_success_1() {
        Points points = new Points(List.of(
                Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30)),
                Point.of(2L, 2000, "테스트", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 8, 31)),
                Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31))
        ));

        Points usablePoints = points.getUsePoints(Point.from(3700));

        Point expected1 = Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31));
        Point expected2 = Point.of(1L, 700, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30));

        assertThat(usablePoints.getPoints()).containsExactly(expected1, expected2);
    }

    @DisplayName("유효기간이 빠른 순으로 사용할 포인트들을 구할 수 있다.(포인트가 딱 떨어질 때)")
    @Test
    void getUsePoints_success_2() {
        Points points = new Points(List.of(
                Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30)),
                Point.of(2L, 2000, "테스트", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 8, 31)),
                Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31))
        ));

        Points usablePoints = points.getUsePoints(Point.from(4000));

        Point expected1 = Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31));
        Point expected2 = Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30));

        assertThat(usablePoints.getPoints()).containsExactly(expected1, expected2);
    }

    @DisplayName("현재 가지고 있는 포인트보다 더 많은 포인트를 사용하는 경우 예외가 발생한다.")
    @Test
    void getUsePoints_fail() {
        Points points = new Points(List.of(
                Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30)),
                Point.of(2L, 2000, "테스트", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 8, 31)),
                Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31))
        ));

        assertThatThrownBy(() -> points.getUsePoints(Point.from(50000)))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("사용가능한 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
    }
}
